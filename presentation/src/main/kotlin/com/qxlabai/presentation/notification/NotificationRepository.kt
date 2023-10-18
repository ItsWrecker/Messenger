package com.qxlabai.presentation.notification

import android.app.Notification
import org.hamcrest.core.SubstringMatcher

interface NotificationRepository {

    fun getNotification(
        title: String,
        text: String
    ): Notification

    fun sendNotification(
        id: Int,
        notification: Notification
    )
}