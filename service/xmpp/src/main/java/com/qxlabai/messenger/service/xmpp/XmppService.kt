package com.qxlabai.messenger.service.xmpp

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import dagger.hilt.android.AndroidEntryPoint
import com.qxlabai.messenger.core.data.repository.PreferencesRepository
import com.qxlabai.messenger.service.xmpp.collector.AccountsCollector
import com.qxlabai.messenger.service.xmpp.notification.NotificationManager
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.jivesoftware.smackx.push_notifications.PushNotificationsManager

@AndroidEntryPoint
class XmppService : Service() {

    private val scope = CoroutineScope(SupervisorJob())

    @Inject
    lateinit var xmppManager: XmppManager

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    @Inject
    lateinit var accountsCollector: AccountsCollector

    @Inject
    lateinit var notificationManager: NotificationManager

    @RequiresApi(Build.VERSION_CODES.Q)
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

    private suspend fun observeAccountsStream() {
        accountsCollector.collectAccounts(
            onNewLogin = {
                xmppManager.login(it)
                xmppManager.purseDevice()
            },
            onNewRegister = {
                Log.e("Register", "$it")
                xmppManager.register(it)
                xmppManager.purseDevice()
            },
            onLogout = {
                Log.e("Logout", "logout")
                xmppManager.logout()
                preferencesRepository.logout()
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun startForeground() {
        val notification = notificationManager.getNotification(
            title = "Messenger",
            text = "Xmpp service is running"
        )
        startForeground(1000, notification)
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onDestroy() {
        scope.cancel()
        xmppManager.onCleared()
        super.onDestroy()
    }
}
