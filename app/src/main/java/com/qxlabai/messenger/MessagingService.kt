package com.qxlabai.messenger

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class MessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.e("NEW_TOKEN", token)
    }

}