package com.qxlabai.presentation.xmpp.services

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.qxlabai.domain.interactors.xmpp.ConnectXmppUseCase
import com.qxlabai.presentation.core.Store
import com.qxlabai.presentation.xmpp.auth.AuthAction
import com.qxlabai.presentation.xmpp.auth.AuthState
import com.qxlabai.presentation.xmpp.connection.XmppAction
import com.qxlabai.presentation.xmpp.connection.XmppState
import com.qxlabai.presentation.xmpp.profile.ProfileAction
import com.qxlabai.presentation.xmpp.profile.ProfileState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.SmackConfiguration
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.android.AndroidSmackInitializer
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jxmpp.jid.DomainBareJid
import org.jxmpp.jid.impl.JidCreate
import javax.inject.Inject


@Suppress("DEPRECATION")
@AndroidEntryPoint
class XmppService : LifecycleService(), ConnectionListener {


    private lateinit var connection: AbstractXMPPConnection

    @Inject
    lateinit var authStore: Store<AuthState, AuthAction>

    @Inject
    lateinit var connectionStore: Store<XmppState, XmppAction>

    @Inject
    lateinit var profileStore: Store<ProfileState, ProfileAction>

    companion object {
        private const val CHANNEL_ID = "Messenger"
        private const val TIMEOUT = 45_000
        private const val serverAddress = "conversations.im"
        private val xmppServiceDomain: DomainBareJid = JidCreate.domainBareFrom(serverAddress)
        private const val port = 5222
        private const val COMMAND_CONNECT = "EstablishConnection"
        const val COMMAND_AUTH = "Authenticate"
        const val COMMAND_PROFILE = "profile"
        private const val COMMAND_DISCONNECT = "Disconnect"
        const val DATA_CREDENTIALS = "CREDENTIALS"

        fun getIntent(context: Context): Intent {
            return Intent(context, this::class.java)
        }
    }

    @Inject
    lateinit var connectXmppUseCase: ConnectXmppUseCase


    private val binder: XmppBinder = XmppBinder()

    inner class XmppBinder : Binder() {
        fun getService(): XmppService = this@XmppService

        fun authenticate(credentialsParcel: CredentialsParcel) {
            this@XmppService.startService(Intent(this@XmppService, this::class.java).also {
                it.action = COMMAND_AUTH
            })
        }
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        SmackConfiguration.DEBUG = true
        AndroidSmackInitializer.initialize(this)
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        SmackConfiguration.DEBUG = true
        AndroidSmackInitializer.initialize(this)
        this.startService(Intent(this, this::class.java).also {
            it.action = COMMAND_CONNECT
        })
        Log.i("XMPP", "Service started")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action ?: return super.onStartCommand(intent, flags, startId)
        when (action) {
            COMMAND_CONNECT -> {
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val config = XMPPTCPConnectionConfiguration.builder()
                            .setConnectTimeout(60_000)
                            .setXmppDomain(xmppServiceDomain)
                            .setPort(port)
                            .setSecurityMode(ConnectionConfiguration.SecurityMode.required)
                            .build()
                        connection = XMPPTCPConnection(config)
                        connection.addConnectionListener(this@XmppService)
                        if (connection.isConnected.not()) {
                            connection.connect()

                        } else return@launch
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                        return@launch
                    }
                }

            }

            COMMAND_DISCONNECT -> {
                if (connection.isConnected) connection.disconnect()
            }

            COMMAND_AUTH -> {
                val credentialsParcel = CredentialsParcel(
                    authStore.state.value.uuid,
                    authStore.state.value.passcode
                )
                if (connection.isConnected && connection.isAuthenticated.not())
                    connection.login(credentialsParcel.uuid, credentialsParcel.passcode)
                Log.e("AUTH", "$credentialsParcel")
            }

            COMMAND_PROFILE -> {
                if (connection.isConnected && connection.isAuthenticated) {
                    val user = connection.user.resourceOrEmpty
                    lifecycleScope.launch {
                        return@launch profileStore.dispatch(ProfileAction.Profile(user.toString()))
                    }
                }
            }
        }
        return START_STICKY
    }

    override fun connected(connection: XMPPConnection?) {
        super.connected(connection)
        lifecycleScope.launch(Dispatchers.IO) {
            connection?.let { XmppAction.Connected(it) }
                ?.let { connectionStore.dispatch(it) }
        }
    }

    override fun connecting(connection: XMPPConnection?) {
        super.connecting(connection)
        lifecycleScope.launch(Dispatchers.IO) {
            connection?.let {
                XmppAction.Connecting(connection)
            }?.let { connectionStore.dispatch(it) }
        }
    }

    override fun authenticated(connection: XMPPConnection?, resumed: Boolean) {
        super.authenticated(connection, resumed)
        lifecycleScope.launch(Dispatchers.IO) {
            authStore.dispatch(AuthAction.Authenticated)
        }
    }
}