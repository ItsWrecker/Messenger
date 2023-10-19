package com.qxlabai.messenger.service.xmpp

import android.util.Log
import com.qxlabai.messenger.core.model.data.Account
import com.qxlabai.messenger.core.model.data.AccountStatus.Online
import com.qxlabai.messenger.core.model.data.AccountStatus.ServerNotFound
import com.qxlabai.messenger.core.model.data.AccountStatus.Unauthorized
import com.qxlabai.messenger.core.model.data.ConnectionStatus
import com.qxlabai.messenger.core.data.repository.PreferencesRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import org.jivesoftware.smack.ReconnectionManager
import org.jivesoftware.smack.ReconnectionManager.ReconnectionPolicy.FIXED_DELAY
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jxmpp.jid.parts.Resourcepart

private const val TAG = "XmppManagerImpl"

class XmppManagerImpl @Inject constructor(
    private val rosterManager: RosterManager,
    private val messageManager: MessageManager,
    private val preferencesRepository: PreferencesRepository,
    private val ioDispatcher: CoroutineDispatcher
) : XmppManager {

    private var xmppConnection: XMPPTCPConnection? = null

    private var account: Account? = null

    private val connectionListener = SimpleConnectionListener()

    override suspend fun initialize() {
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
        TODO("Not yet implemented")
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
            .build()

    // connect and login are called with Dispatchers.IO context
    private suspend fun XMPPTCPConnection.connectAndLogin(): Result<XMPPTCPConnection> =
        runCatching {
            withContext(ioDispatcher) {
                connect()
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
                messageManager.initialize(connection)
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
}
