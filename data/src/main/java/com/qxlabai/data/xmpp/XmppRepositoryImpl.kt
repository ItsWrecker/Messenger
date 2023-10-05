package com.qxlabai.data.xmpp


import android.content.Context
import com.qxlabai.data.di.XmppModule
import com.qxlabai.domain.events.Events
import com.qxlabai.domain.repositories.XmppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.SASLAuthentication
import org.jivesoftware.smack.SmackConfiguration
import org.jivesoftware.smack.android.AndroidSmackInitializer
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.iqregister.AccountManager
import org.jxmpp.jid.DomainBareJid
import org.jxmpp.jid.impl.JidCreate
import org.jxmpp.jid.parts.Localpart
import java.net.InetAddress
import javax.inject.Inject

class XmppRepositoryImpl @Inject constructor(
    context: Context,
    private val accountManager: AccountManager,
    private val chatManager: ChatManager
) : XmppRepository {

    companion object {
        private const val TIMEOUT = 45_000
        private const val serverAddress = "conversations.im"
        private val xmppServiceDomain: DomainBareJid = JidCreate.domainBareFrom(serverAddress)
        private const val port = 5222
    }

    init {
        SmackConfiguration.DEBUG = true
        AndroidSmackInitializer.initialize(context)

    }

    private lateinit var connection: AbstractXMPPConnection

    override suspend fun connect(): Flow<Events<Boolean>> = flow {

        val config = XMPPTCPConnectionConfiguration.builder()
            .setConnectTimeout(60_000)
            .setXmppDomain(xmppServiceDomain)

//            .setHostAddress(InetAddress.getByName(serverAddress))
            .setPort(port)
            .setSecurityMode(ConnectionConfiguration.SecurityMode.required)
            .build()
        connection = XMPPTCPConnection(config)
        emit(Events.Loading("Establishing the connection"))
        try {
            if (connection.isConnected.not()) connection.connect()
            return@flow emit(Events.Success(connection.isConnected))
        } catch (exception: Exception) {
            return@flow emit(Events.Error("Error while establishment of connection", exception))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun disconnect(): Boolean {
        return try {
            if (connection.isConnected) connection.disconnect()
            !connection.isConnected
        } catch (exception: Exception) {
            false
        }
    }

    override suspend fun authenticate(username: String, passcode: String): Flow<Events<String>> =
        flow {
            try {
                emit(Events.Loading("Authenticating the user"))
                if (connection.isConnected) {
                    SASLAuthentication.unBlacklistSASLMechanism("PLAIN")
                    SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5")
                    connection.login(username, passcode)
                    return@flow emit(Events.Success(connection.isAuthenticated.toString()))
                } else {
                    connection.connect()
                    SASLAuthentication.unBlacklistSASLMechanism("PLAIN")
                    SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5")
                    connection.login(username, passcode)
                    return@flow emit(Events.Success(connection.isAuthenticated.toString()))
                }
//                val recipientJID = JidCreate.entityBareFrom("santosh@conversations.im")
//                return@flow emit(Events.Error("Username or passcode is invalid"))
            } catch (exception: Exception) {
                return@flow emit(
                    Events.Error(
                        "Something went wrong while authenticating, please try after sometime",
                        exception
                    )
                )
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun createAccount(username: String, passcode: String): Flow<Events<String>> =
        flow {
            try {
                emit(Events.Loading("Creating the new User please wait.."))
                accountManager.createAccount(Localpart.from(username), passcode)
                return@flow emit(Events.Success("Success"))
            } catch (exception: Exception) {
                return@flow emit(Events.Error("Error while creating the new account", exception))
            }
        }

    override suspend fun getUserId(): Flow<Events<String>> = flow {
        emit(Events.Loading("Fetching the user id."))
        try {
            return@flow emit(
                Events.Success(
                    when (connection.isConnected && connection.isAuthenticated) {
                        true -> connection.user.toString()
                        else -> ""
                    }
                )
            )
        } catch (exception: Exception) {
            return@flow emit(Events.Error("Error while fetching the userId", cause = exception))
        }
    }

}