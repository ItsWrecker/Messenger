package com.qxlabai.messenger.service.xmpp

import com.qxlabai.messenger.service.xmpp.notification.NotificationManager
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.roster.SubscribeListener
import org.jxmpp.jid.Jid
import javax.inject.Inject

class SubscriptionManagerImpl @Inject constructor(
    private val notificationManager: NotificationManager
) : SubscriptionManager {

    override suspend fun getSubscribeListener(): SubscribeListener {
        return SubscribeListener { from, subscribeRequest ->
            return@SubscribeListener handleSubscription(from, subscribeRequest)
        }
    }

    private fun handleSubscription(
        from: Jid,
        subscribeRequest: Presence
    ): SubscribeListener.SubscribeAnswer {
        println("$from, $subscribeRequest")
        val notification = notificationManager.getNotification("Subscription", "$from")
        notificationManager.sendNotification(100, notification)
        return SubscribeListener.SubscribeAnswer.Approve
    }
}