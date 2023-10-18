package com.qxlabai.presentation.connection

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.qxlabai.domain.collectors.AccountsCollector
import com.qxlabai.domain.repositories.PreferencesRepository
import com.qxlabai.presentation.notification.NotificationRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class XmppConnectionService : Service() {


    companion object {
        private val TAG = XmppConnectionService::class.java.simpleName
    }

    private val scope: CoroutineScope by lazy {
        return@lazy CoroutineScope(SupervisorJob())
    }

    @Inject
    lateinit var xmppManager: XmppManager

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    @Inject
    lateinit var accountsCollector: AccountsCollector

    @Inject
    lateinit var notificationManager: NotificationRepository

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        startForeground()

        scope.launch {
            xmppManager.initialize()
        }

        scope.launch {
            observeAccountsStream()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForeground() {
        val notification = notificationManager.getNotification(
            title = "Messenger Service",
            text = "Xmpp service is running"
        )
        startForeground(1000, notification)
    }

    override fun onDestroy() {
        Log.e(TAG, "Service Destroyed")
        scope.cancel()
        xmppManager.onCleared()
        super.onDestroy()
    }

    private suspend fun observeAccountsStream() {
        accountsCollector.collectAccounts(
            onNewLogin = { xmppManager.login(it) },
            onNewRegister = { xmppManager.register(it) },
        )
    }
}