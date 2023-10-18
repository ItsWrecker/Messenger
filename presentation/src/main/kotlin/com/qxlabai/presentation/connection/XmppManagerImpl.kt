package com.qxlabai.presentation.connection

import android.util.Log
import com.qxlabai.domain.entity.Account
import com.qxlabai.domain.entity.AccountStatus
import com.qxlabai.domain.entity.ConnectionStatus
import com.qxlabai.domain.repositories.PreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.ReconnectionManager
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jxmpp.jid.parts.Resourcepart
import javax.inject.Inject

class XmppManagerImpl @Inject constructor(
    private val rosterManager: RosterManager,
    private val messageManager: MessageManager,
    private val preferencesRepository: PreferencesRepository,
    private val ioDispatcher: CoroutineDispatcher
) : XmppManager {

    companion object {
        private val TAG = XmppManager::class.java.simpleName
    }

    private var xmppConnection: XMPPTCPConnection? = null
    private var account: Account? = null

    private var connectionListener: ConnectionListener = SimpleConnectionListener()

    override suspend fun initialize() {

        if (xmppConnection == null) {
            preferencesRepository.updateConnectionStatus(ConnectionStatus())

            val exitedAccount = preferencesRepository.getAccount().firstOrNull()
            exitedAccount?.let {
                if (it.alreadyLoggedIn) {
                    login(it)
                }
            }
        }
    }

    override fun getConnection(): XMPPTCPConnection {
        return xmppConnection ?: throw NoSuchElementException("Connection is not established.")
    }

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

    private suspend fun XMPPTCPConnection.connectAndLogin(): Result<XMPPTCPConnection> =
        runCatching {
            withContext(ioDispatcher) {
                connect()
                login()
                this@connectAndLogin
            }
        }

    private fun configureReconnectionManager(connection: XMPPTCPConnection) {
        Log.d(TAG, "Configure reconnection manager")
        val reconnectionManager = ReconnectionManager.getInstanceFor(connection)
        reconnectionManager.setReconnectionPolicy(ReconnectionManager.ReconnectionPolicy.FIXED_DELAY)
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

    private suspend fun Account.connectionFailureHandler(throwable: Throwable?) {
        when (throwable) {
            is SmackException.EndpointConnectionException -> {
                preferencesRepository.updateAccounts(this.copy(status = AccountStatus.ServerNotFound))
            }

            else -> {
                preferencesRepository.updateAccounts(this.copy(status = AccountStatus.Unauthorized))
            }
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

            AccountStatus.Online
        } else {
            AccountStatus.Unauthorized
        }

        preferencesRepository.updateAccounts(this.copy(status = status))

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
}