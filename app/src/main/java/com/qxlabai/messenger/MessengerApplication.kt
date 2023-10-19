package com.qxlabai.messenger

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.jivesoftware.smack.android.AndroidSmackInitializer

@HiltAndroidApp
class MessengerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AndroidSmackInitializer.initialize(this)
    }
}
