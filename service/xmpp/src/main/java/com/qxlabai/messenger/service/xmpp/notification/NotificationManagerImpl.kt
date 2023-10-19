package com.qxlabai.messenger.service.xmpp.notification

import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.qxlabai.messenger.service.xmpp.R
import javax.inject.Inject
import android.app.NotificationManager as AndroidNotificationManager

const val CHANNEL_ID = "xmpp_service_channel_id"

class NotificationManagerImpl @Inject constructor(
    private val context: Context
) : NotificationManager {

    override fun getNotification(title: String, text: String): Notification {
        createNotificationChannel()
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_chat)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setAutoCancel(true)
            .build()
    }

    override fun sendNotification(id: Int, notification: Notification) {
        with(NotificationManagerCompat.from(context)) {
            notify(id, notification)
        }
    }

    private fun createNotificationChannel() {
        val name = context.getString(R.string.channel_name)
        val descriptionText = context.getString(R.string.channel_description)
        val importance = AndroidNotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: AndroidNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as AndroidNotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
