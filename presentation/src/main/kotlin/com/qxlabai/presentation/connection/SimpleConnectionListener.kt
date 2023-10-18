package com.qxlabai.presentation.connection

import android.util.Log
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.XMPPConnection

class SimpleConnectionListener : ConnectionListener {

    companion object {
        private const val TAG = "ConnectionListener"
    }
    override fun connecting(connection: XMPPConnection?) {
        Log.d(TAG, "connecting... (ConnectionListener)")
    }

    override fun connected(connection: XMPPConnection?) {
        Log.d(TAG, "connected (ConnectionListener)")
    }

    override fun authenticated(connection: XMPPConnection?, resumed: Boolean) {
        Log.d(TAG, "authenticated (ConnectionListener), resumed: $resumed")
    }

    override fun connectionClosed() {
        Log.d(TAG, "connectionClosed (ConnectionListener)")
    }

    override fun connectionClosedOnError(e: Exception?) {
        Log.d(TAG, "connectionClosedOnError (ConnectionListener) with error: $e")
    }
}