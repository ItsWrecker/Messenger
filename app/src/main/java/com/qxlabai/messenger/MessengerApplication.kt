package com.qxlabai.messenger

import android.app.Application
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import org.jivesoftware.smack.android.AndroidSmackInitializer
import org.jivesoftware.smackx.omemo.signal.SignalOmemoService
import org.unifiedpush.android.connector.UnifiedPush


@HiltAndroidApp
class MessengerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        try {
            try {
                AndroidSmackInitializer.initialize(this)
                SignalOmemoService.acknowledgeLicense()
                SignalOmemoService.setup()

            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            Firebase.initialize(this)
            FirebaseMessaging.getInstance().token.let {
                Log.e("TOKEN", it.result)
            }

        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}
