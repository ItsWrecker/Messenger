package com.qxlabai.messenger

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.jivesoftware.smack.android.AndroidSmackInitializer
import org.jivesoftware.smackx.omemo.signal.SignalOmemoService


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

        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}
