package com.qxlabai.presentation.xmpp.services

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService

class XmppService : LifecycleService() {


    private val binder: XmppBinder = XmppBinder()

    inner class XmppBinder : Binder() {
        fun getService(): XmppService = this@XmppService
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        Log.e(XmppService::class.java.simpleName, "Created")
    }


}