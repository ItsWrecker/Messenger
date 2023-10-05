package com.qxlabai.presentation.xmpp.services

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService
import com.qxlabai.domain.interactors.xmpp.ConnectXmppUseCase
import dagger.hilt.android.AndroidEntryPoint
import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.SmackConfiguration
import org.jivesoftware.smack.android.AndroidSmackInitializer
import org.jxmpp.jid.DomainBareJid
import org.jxmpp.jid.impl.JidCreate
import javax.inject.Inject


@AndroidEntryPoint
class XmppService : LifecycleService(), ConnectionListener {


    private lateinit var connection: AbstractXMPPConnection

    companion object {
        private const val TIMEOUT = 45_000
        private const val serverAddress = "conversations.im"
        private val xmppServiceDomain: DomainBareJid = JidCreate.domainBareFrom(serverAddress)
        private const val port = 5222
    }

    @Inject
    lateinit var connectXmppUseCase: ConnectXmppUseCase


    private val binder: XmppBinder = XmppBinder()

    inner class XmppBinder : Binder() {
        fun getService(): XmppService = this@XmppService

    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        SmackConfiguration.DEBUG = true
        AndroidSmackInitializer.initialize(this)
        return binder
    }

    override fun onCreate() {
        super.onCreate()

        Log.e(XmppService::class.java.simpleName, "Created")
    }
}