package com.qxlabai.messenger.service.xmpp.notification

import android.app.Notification

interface NotificationManager {

    fun getNotification(title: String, text: String): Notification

    fun sendNotification(id: Int, notification: Notification)
}
