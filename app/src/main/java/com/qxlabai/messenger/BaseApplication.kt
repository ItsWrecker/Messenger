package com.qxlabai.messenger

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import dagger.hilt.android.HiltAndroidApp
import org.jivesoftware.smack.android.AndroidSmackInitializer

@HiltAndroidApp
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        AndroidSmackInitializer.initialize(this)
    }


    private fun createNotificationChannel() {
        val channelId = "my_notification_channel_id"
        val channelName = "My Notification Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance)
        channel.enableLights(true)
        channel.lightColor = Color.RED
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}