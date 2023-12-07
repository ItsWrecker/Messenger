package com.qxlabai.messenger.service.xmpp.unifiedpush

import android.content.Context
import org.unifiedpush.android.embedded_fcm_distributor.EmbeddedDistributorReceiver


class EmbeddedDistributor : EmbeddedDistributorReceiver() {

    override fun getEndpoint(context: Context, fcmToken: String, instance: String): String {
        return "https://fcm.wrecker.in/FCM?v2&instance=$instance&token=$fcmToken"
    }
}
