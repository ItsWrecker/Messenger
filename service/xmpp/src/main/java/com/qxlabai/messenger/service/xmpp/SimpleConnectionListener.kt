package com.qxlabai.messenger.service.xmpp

import android.util.Log
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smackx.iqregister.AccountManager
import org.jxmpp.jid.parts.Localpart

private const val TAG = "ConnectionListener"

class SimpleConnectionListener : ConnectionListener {
    override fun connecting(connection: XMPPConnection?) {
        Log.d(TAG, "connecting... (ConnectionListener)")
    }

    override fun connected(connection: XMPPConnection?) {
        Log.d(TAG, "connected (ConnectionListener)")
//        try {
//
//            val accountManager = AccountManager.getInstance(connection)
//            accountManager.createAccount(Localpart.from("user_ten"), "12345678")
//            accountManager.supportsAccountCreation().let {
//                Log.e(TAG, it.toString())
//            }
//        } catch (exception: Exception) {
//            Log.e(TAG, exception.message, exception)
//        }
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
