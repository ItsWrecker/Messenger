package com.qxlabai.messenger.service.xmpp

import com.qxlabai.messenger.core.data.repository.SubscriptionRepository
import com.qxlabai.messenger.core.model.data.Subscription
import com.qxlabai.messenger.service.xmpp.collector.SubscriptionCollector
import com.qxlabai.messenger.service.xmpp.notification.NotificationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.roster.SubscribeListener
import org.jxmpp.jid.Jid
import javax.inject.Inject

class SubscriptionManagerImpl @Inject constructor(
    private val notificationManager: NotificationManager,
    private val subscriptionRepository: SubscriptionRepository,
    private val subscriptionCollector: SubscriptionCollector
) : SubscriptionManager {
    private val scope = CoroutineScope(SupervisorJob())


    override suspend fun getSubscribeListener(): SubscribeListener {
        scope.launch {
            subscriptionCollector.collectIsApproved()
        }
        return SubscribeListener { from, subscribeRequest ->
            return@SubscribeListener handleSubscription(from, subscribeRequest)
        }
    }

    private fun handleSubscription(
        from: Jid, subscribeRequest: Presence
    ): SubscribeListener.SubscribeAnswer {
        println("$from, $subscribeRequest")

        scope.launch {
            if (subscribeRequest.type == Presence.Type.subscribe) {
                val subscription =
                    subscriptionRepository.getSubscription(jid = from.asBareJid().toString())
                if (subscription == null || subscription.isPending) {
                    val notification = notificationManager.getNotification("Subscription", "$from")
                    notificationManager.sendNotification(100, notification)
                    subscriptionRepository.updateSubscription(
                        listOf(
                            Subscription(
                                subscriptionFrom = from.asBareJid().toString(), isApproved = false
                            )
                        )
                    )
                }

            }
        }
        return SubscribeListener.SubscribeAnswer.Approve
    }
}