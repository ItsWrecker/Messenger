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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.ReconnectionManager
import org.jivesoftware.smack.ReconnectionManager.ReconnectionPolicy.FIXED_DELAY
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.packet.Stanza
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.carbons.packet.CarbonExtension
import org.jivesoftware.smackx.omemo.OmemoConfiguration
import org.jivesoftware.smackx.omemo.OmemoManager
import org.jivesoftware.smackx.omemo.OmemoMessage
import org.jivesoftware.smackx.omemo.listener.OmemoMessageListener
import org.jivesoftware.smackx.omemo.signal.SignalCachingOmemoStore
import org.jivesoftware.smackx.omemo.signal.SignalFileBasedOmemoStore
import org.jivesoftware.smackx.omemo.signal.SignalOmemoService
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

    override suspend fun initialize() {


        try {
            signaleOmemoService = SignalOmemoService.getInstance() as SignalOmemoService
            signaleOmemoService.omemoStoreBackend =
                SignalCachingOmemoStore(SignalFileBasedOmemoStore(context.filesDir))
            OmemoConfiguration.setAddOmemoHintBody(false)
        } catch (exception: Exception) {
            exception.printStackTrace()
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
            configurationBuilder = ::getConfiguration,
            connectionBuilder = ::XMPPTCPConnection,
            connectionListener = ::addConnectionListener
        )
    }

    private fun Account.register(
        configurationBuilder: (Account) -> XMPPTCPConnectionConfiguration,
        connectionBuilder: (XMPPTCPConnectionConfiguration) -> XMPPTCPConnection,
        connectionListener: (XMPPTCPConnection) -> Unit,
    ): XMPPTCPConnection {
        val configuration = configurationBuilder(this)
        val connection = connectionBuilder(configuration)
        connectionListener(connection)
        return connection
    }

    private val omemoMessageListener = object : OmemoMessageListener {
        override fun onOmemoMessageReceived(
            stanza: Stanza?,
            decryptedMessage: OmemoMessage.Received?
        ) {
            Log.e(TAG, "Received")
            Log.e(TAG, "${decryptedMessage?.body}")
            try {
                messageManager.handleIncomingMessage(
                    stanza,
                    decryptedMessage
                )
            }catch (exception: Exception){
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

        val result = connection.connectAndLogin()

        return if (result.isSuccess) {

            if (connection.isAuthenticated) {
                //omemoManager.purgeDeviceList()
                omemoManager.initializeAsync(this@XmppManagerImpl)
                omemoManager.trustOmemoIdentity(omemoManager.ownDevice, omemoManager.ownFingerprint)
            } else {
                Log.e(TAG, "connection must be initialized")
            }
            rosterManager.clearContacts(connection)
            successHandler(result.getOrThrow())
        } else {
            failureHandler(result.exceptionOrNull())
            null
        }
    }

    private fun getConfiguration(account: Account): XMPPTCPConnectionConfiguration =
        XMPPTCPConnectionConfiguration.builder()
            .setUsernameAndPassword(account.localPart, account.password)
            .setXmppDomain(account.domainPart)
            .setResource(Resourcepart.from("Android"))
            .setSecurityMode(ConnectionConfiguration.SecurityMode.required)
            .build()

    // connect and login are called with Dispatchers.IO context
    private suspend fun XMPPTCPConnection.connectAndLogin(): Result<XMPPTCPConnection> =
        runCatching {
            withContext(ioDispatcher) {
                connect()
                omemoManager = OmemoManager.getInstanceFor(this@runCatching)
                omemoManager.setTrustCallback(EphemeralTrustCallback())
                omemoManager.addOmemoMessageListener(omemoMessageListener)
                login()
                this@connectAndLogin
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

    }

    override fun initializationFinished(manager: OmemoManager?) {

    }
}
