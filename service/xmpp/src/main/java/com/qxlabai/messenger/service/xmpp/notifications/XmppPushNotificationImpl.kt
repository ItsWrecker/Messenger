package com.qxlabai.messenger.service.xmpp.notifications

import android.content.Context
import android.util.Log
import com.qxlabai.messenger.service.xmpp.unifiedpush.Store
import dagger.hilt.android.qualifiers.ApplicationContext
import org.jivesoftware.smack.packet.IQ
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smackx.commands.AdHocCommand
import org.jivesoftware.smackx.commands.packet.AdHocCommandData
import org.jivesoftware.smackx.push_notifications.PushNotificationsManager
import org.jivesoftware.smackx.xdata.FormField
import org.jivesoftware.smackx.xdata.packet.DataForm
import org.unifiedpush.android.connector.UnifiedPush
import javax.inject.Inject
import kotlin.random.Random


class XmppPushNotificationImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : XmppPushNotification {

    companion object {
        private val TAG = XmppPushNotification::class.java.simpleName
    }

    private lateinit var pushNotificationsManager: PushNotificationsManager

    override fun initialize(xmppConnection: XMPPTCPConnection) {
//        FirebaseMessaging.getInstance().token.addOnCompleteListener {
//            if (it.isSuccessful) {
//                try {
//                    registerTokenToXmpp(
//                        it.result,
//                        xmppConnection
//                    )
//
//                    val message = Message(xmppConnection.user.asBareJid())
//                } catch (exception: Exception) {
//                    Log.e(TAG, exception.message, exception)
//                }
//            } else {
//                Log.e(TAG, "Token not found")
//            }
//        }


//       CoroutineScope(Dispatchers.Main).launch {
//           if (store.featureByteMessage) {
//               UnifiedPush.registerAppWithDialog(
//                   context,
//                   features = arrayListOf(UnifiedPush.FEATURE_BYTES_MESSAGE)
//               )
//           } else {
//               UnifiedPush.registerAppWithDialog(context)
//           }
//       }
        try {
            val store = Store(context)
            if (UnifiedPush.getDistributor(context).isNotEmpty()) {
                if (store.featureByteMessage){
                    UnifiedPush.registerApp(
                        context,
                        features = arrayListOf(UnifiedPush.FEATURE_BYTES_MESSAGE)
                    )
                }else{
                    UnifiedPush.registerApp(context)
                }
                return
            }
            val distributors = UnifiedPush.getDistributors(context)
            UnifiedPush.saveDistributor(context, distributors.first())
            UnifiedPush.registerApp(context)
        } catch (exception: Exception) {
            Log.e(TAG, exception.message, exception)
        }
    }

    private fun registerTokenToXmpp(
        deviceToken: String, xmppConnection: XMPPTCPConnection
    ) {
        val dataForm = DataForm.builder(DataForm.Type.submit)
        val token = FormField.builder("token").setValue(deviceToken).build()
        val deviceId =
            FormField.builder("device_id").setValue(Random.Default.nextInt(100000, 999999)).build()
        val deviceName = FormField.builder("device_name").setValue("android").build()
        dataForm.addField(token)
        dataForm.addField(deviceId)
        dataForm.addField(deviceName)
        val stanza = AdHocCommandData()
        stanza.to = xmppConnection.user.asDomainBareJid()
        stanza.type = IQ.Type.set
        stanza.stanzaId = "0043423"
        stanza.node = "register-push-gcm"
        stanza.action = AdHocCommand.Action.execute
        stanza.form = dataForm.build()
        stanza.from = xmppConnection.user

        Log.e("STANZAS", stanza.toXML().toString())

        try {
            if (xmppConnection.isSmEnabled) {
                xmppConnection.addStanzaIdAcknowledgedListener(stanza.stanzaId) {
                    Log.e(TAG, it.toString())
                }
                xmppConnection.sendStanza(stanza)
            }
        } catch (exception: Exception) {
            Log.e(TAG, exception.message, exception)
        }
    }

}