package com.qxlabai.messenger

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BroadcastReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_APP_DATA_CLEARED = "com.qxlabai.messenger.ACTION_APP_DATA_CLEARED"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(ACTION_APP_DATA_CLEARED)) {
            Log.e("RESTART", "RRestart")
            context?.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}