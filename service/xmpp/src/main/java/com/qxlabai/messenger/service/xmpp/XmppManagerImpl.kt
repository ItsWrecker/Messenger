package com.qxlabai.messenger.service.xmpp

import android.content.Context
import android.util.Log
import com.qxlabai.messenger.core.model.data.Account
import com.qxlabai.messenger.core.model.data.AccountStatus.Online
import com.qxlabai.messenger.core.model.data.AccountStatus.ServerNotFound
import com.qxlabai.messenger.core.model.data.AccountStatus.Unauthorized
import com.qxlabai.messenger.core.model.data.ConnectionStatus
import com.qxlabai.messenger.core.data.repository.PreferencesRepository
import com.qxlabai.messenger.service.xmpp.omemo.EphemeralTrustCallback
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.ReconnectionManager
import org.jivesoftware.smack.ReconnectionManager.ReconnectionPolicy.FIXED_DELAY
import org.jivesoftware.smack.SmackConfiguration
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.packet.Stanza
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.carbons.packet.CarbonExtension
import org.jivesoftware.smackx.iqregister.AccountManager
import org.jivesoftware.smackx.omemo.OmemoConfiguration
import org.jivesoftware.smackx.omemo.OmemoManager
import org.jivesoftware.smackx.omemo.OmemoMessage
import org.jivesoftware.smackx.omemo.listener.OmemoMessageListener
import org.jivesoftware.smackx.omemo.signal.SignalCachingOmemoStore
import org.jivesoftware.smackx.omemo.signal.SignalFileBasedOmemoStore
import org.jivesoftware.smackx.omemo.signal.SignalOmemoService
import org.jxmpp.jid.parts.Localpart
import org.jxmpp.jid.parts.Resourcepart

private const val TAG = "XmppManagerImpl"

class XmppManagerImpl @Inject constructor(
    private val rosterManager: RosterManager,
    private val messageManager: MessageManager,
    private val preferencesRepository: PreferencesRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val context: Context
) : XmppManager, OmemoManager.InitializationFinishedCallback {

    private var xmppConnection: XMPPTCPConnection? = null

    private var account: Account? = null

    private val connectionListener = SimpleConnectionListener()

    private lateinit var omemoManager: OmemoManager
    private lateinit var signaleOmemoService: SignalOmemoService
    var omemoStoreBackendSet = false

    override suspend fun initialize() {
        SmackConfiguration.DEBUG = true

        try {
            if (this::signaleOmemoService.isInitialized.not()) {
                signaleOmemoService = SignalOmemoService.getInstance() as SignalOmemoService
                OmemoConfiguration.setAddOmemoHintBody(false)
            }
            if (!omemoStoreBackendSet) {
                signaleOmemoService.omemoStoreBackend =
                    SignalCachingOmemoStore(SignalFileBasedOmemoStore(context.filesDir))
                omemoStoreBackendSet = true
            }
        } catch (exception: Exception) {
            Log.e(TAG, exception.message, exception)
        }

        if (xmppConnection == null) {
            preferencesRepository.updateConnectionStatus(ConnectionStatus())

            val existedAccount = preferencesRepository.getAccount().firstOrNull()
            existedAccount?.let {
                if (it.alreadyLoggedIn) {
                    login(it)
                }
            }
        }
    }

    override fun getConnection(): XMPPTCPConnection =
        xmppConnection ?: throw NoSuchElementException("Connection is not established.")

    override suspend fun login(account: Account) {
        this.account = account
        xmppConnection = account.login(
            configurationBuilder = ::getConfiguration,
            connectionBuilder = ::XMPPTCPConnection,
            connectionListener = ::addConnectionListener,
            successHandler = { account.connectionSuccessHandler(it) },
            failureHandler = { account.connectionFailureHandler(it) }
        )
    }

    override suspend fun register(account: Account) {
        this.account = account
        xmppConnection = account.register(
            configurationBuilder = ::getConfigurationForRegister,
            connectionBuilder = ::XMPPTCPConnection,
            connectionListener = ::addConnectionListener,
            successHandler = { account.connectionSuccessHandler(it) },
            failureHandler = { account.connectionFailureHandler(it) }
        )
    }


    private val omemoMessageListener = object : OmemoMessageListener {
        override fun onOmemoMessageReceived(
            stanza: Stanza?,
            decryptedMessage: OmemoMessage.Received?
        ) {
            Log.e(TAG, decryptedMessage?.body.toString())

            try {
                messageManager.handleIncomingMessage(
                    stanza,
                    decryptedMessage
                )
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }

        override fun onOmemoCarbonCopyReceived(
            direction: CarbonExtension.Direction?,
            carbonCopy: Message?,
            wrappingMessage: Message?,
            decryptedCarbonCopy: OmemoMessage.Received?
        ) {
            Log.e(TAG, "message")
        }

    }

    private suspend fun Account.register(
        configurationBuilder: (Account) -> XMPPTCPConnectionConfiguration,
        connectionBuilder: (XMPPTCPConnectionConfiguration) -> XMPPTCPConnection,
        connectionListener: (XMPPTCPConnection) -> Unit,
        successHandler: suspend Account.(XMPPTCPConnection) -> XMPPTCPConnection,
        failureHandler: suspend Account.(Throwable?) -> Unit
    ): XMPPTCPConnection? {
        val configuration = configurationBuilder(this)
        val connection = connectionBuilder(configuration)
        connectionListener(connection)
        connection.connectOnce()
        val result = connection.register(this)
        return if (result.isSuccess) {
            val loginResult = connection.login(this)
            if (loginResult.isSuccess) {
                if (connection.isAuthenticated) {
                    omemoManager.initializeAsync(this@XmppManagerImpl)
                    omemoManager.trustOmemoIdentity(
                        omemoManager.ownDevice,
                        omemoManager.ownFingerprint
                    )
                } else {
                    Log.e(TAG, "connection must be initialized")
                }
                rosterManager.clearContacts(connection)
                successHandler(result.getOrThrow())
            } else {
                failureHandler(result.exceptionOrNull())
                null
            }
        } else {
            result.exceptionOrNull()?.printStackTrace()
            failureHandler(result.exceptionOrNull())
            null
        }
    }

    private suspend fun Account.login(
        configurationBuilder: (Account) -> XMPPTCPConnectionConfiguration,
        connectionBuilder: (XMPPTCPConnectionConfiguration) -> XMPPTCPConnection,
        connectionListener: (XMPPTCPConnection) -> Unit,
        successHandler: suspend Account.(XMPPTCPConnection) -> XMPPTCPConnection,
        failureHandler: suspend Account.(Throwable?) -> Unit
    ): XMPPTCPConnection? {
        val configuration = configurationBuilder(this)
        val connection = connectionBuilder(configuration)
        connectionListener(connection)
        val result = connection.connectOnce()
        return if (result.isSuccess) {
            val loginResult = connection.login(this)
            if (loginResult.isSuccess) {
                if (connection.isAuthenticated) {
                    omemoManager.purgeDeviceList()
                    omemoManager.initializeAsync(this@XmppManagerImpl)
                    omemoManager.trustOmemoIdentity(
                        omemoManager.ownDevice,
                        omemoManager.ownFingerprint
                    )
                } else {
                    Log.e(TAG, "connection must be initialized")
                }
                rosterManager.clearContacts(connection)
                successHandler(result.getOrThrow())
            } else {
                failureHandler(result.exceptionOrNull())
                null
            }
        } else {
            failureHandler(result.exceptionOrNull())
            null
        }
    }

    private fun getConfigurationForRegister(account: Account): XMPPTCPConnectionConfiguration {
        return XMPPTCPConnectionConfiguration.builder()
            .setXmppDomain(account.domainPart)
            .setResource(Resourcepart.from("Android"))
            .setSecurityMode(ConnectionConfiguration.SecurityMode.ifpossible)
            .build()

    }

    private fun getConfiguration(account: Account): XMPPTCPConnectionConfiguration =
        XMPPTCPConnectionConfiguration.builder()
            .setXmppDomain(account.domainPart)
            .setResource(Resourcepart.from("Android"))
            .setSecurityMode(ConnectionConfiguration.SecurityMode.ifpossible)
            .build()

    private suspend fun XMPPTCPConnection.register(account: Account): Result<XMPPTCPConnection> {
        return runCatching {
            withContext(Dispatchers.IO) {
                val accountManager = AccountManager.getInstance(this@runCatching)
                accountManager.createAccount(Localpart.from(account.localPart), account.password)
                return@withContext this@runCatching
            }
        }
    }

    private suspend fun XMPPTCPConnection.connectOnce(): Result<XMPPTCPConnection> {
        return runCatching {
            withContext(Dispatchers.IO) {
                this@runCatching.connect()
                omemoManager =
                    OmemoManager.getInstanceFor(this@runCatching, OmemoManager.randomDeviceId())
                omemoManager.setTrustCallback(EphemeralTrustCallback())
                omemoManager.addOmemoMessageListener(omemoMessageListener)
                return@withContext this@runCatching
            }
        }
    }

    private suspend fun XMPPTCPConnection.login(account: Account): Result<XMPPTCPConnection> =
        runCatching {
            withContext(Dispatchers.IO) {
                this@runCatching.login(account.localPart, account.password)
                return@withContext this@runCatching
            }
        }


    private suspend fun Account.connectionSuccessHandler(
        connection: XMPPTCPConnection
    ): XMPPTCPConnection {
        val status = if (connection.isAuthenticated) {
            configureReconnectionManager(connection)

            withContext(ioDispatcher) {
                rosterManager.initialize(connection)
                messageManager.initialize(connection, omemoManager)
            }

            Online
        } else {
            Unauthorized
        }

        preferencesRepository.updateAccount(this.copy(status = status))

        preferencesRepository.updateConnectionStatus(
            ConnectionStatus(
                availability = true,
                authenticated = connection.isAuthenticated
            )
        )

        Log.d(TAG, "isConnected: ${connection.isConnected}")
        Log.d(TAG, "isAuthenticated: ${connection.isAuthenticated}")

        return connection
    }

    private suspend fun Account.connectionFailureHandler(throwable: Throwable?) {
        when (throwable) {
            is SmackException.EndpointConnectionException -> {
                preferencesRepository.updateAccount(this.copy(status = ServerNotFound))
            }
            // TODO: for now considering other exceptions as authentication failure
            else -> {
                preferencesRepository.updateAccount(this.copy(status = Unauthorized))
            }
        }
    }

    private fun configureReconnectionManager(connection: XMPPTCPConnection) {
        Log.d(TAG, "Configure reconnection manager")
        val reconnectionManager = ReconnectionManager.getInstanceFor(connection)
        reconnectionManager.setReconnectionPolicy(FIXED_DELAY)
        reconnectionManager.setFixedDelay(5)
        reconnectionManager.enableAutomaticReconnection()
    }

    private fun addConnectionListener(connection: XMPPTCPConnection) {
        connection.addConnectionListener(connectionListener)
    }

    override fun onCleared() {
        xmppConnection?.removeConnectionListener(connectionListener)
        rosterManager.onCleared()
        messageManager.onCleared()
    }

    override fun initializationFailed(cause: java.lang.Exception?) {
        Log.e(TAG, cause?.message, cause)

    }

    override fun initializationFinished(manager: OmemoManager?) {
        Log.i(TAG, manager?.ownJid.toString())
    }

    override fun purseDevice() {
        if (xmppConnection?.isAuthenticated == true) omemoManager.purgeDeviceList()
    }
}
