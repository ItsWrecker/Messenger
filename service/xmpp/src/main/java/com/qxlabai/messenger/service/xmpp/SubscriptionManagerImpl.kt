package com.qxlabai.messenger.service.xmpp

import android.util.Log
import com.qxlabai.messenger.core.data.repository.SubscriptionRepository
import com.qxlabai.messenger.core.model.data.Subscription
import com.qxlabai.messenger.service.xmpp.collector.SubscriptionCollector
import com.qxlabai.messenger.service.xmpp.notification.NotificationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.roster.SubscribeListener
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smackx.vcardtemp.VCardManager
import org.jxmpp.jid.Jid
import javax.inject.Inject

class SubscriptionManagerImpl @Inject constructor(
    private val notificationManager: NotificationManager,
    private val subscriptionRepository: SubscriptionRepository,
    private val subscriptionCollector: SubscriptionCollector,
    private val fcmApi: Api
) : SubscriptionManager {
    private val scope = CoroutineScope(SupervisorJob())


    override suspend fun getSubscribeListener(connection: XMPPTCPConnection): SubscribeListener {
        scope.launch {
            subscriptionCollector.collectIsApproved()
        }
        return SubscribeListener { from, subscribeRequest ->
            return@SubscribeListener handleSubscription(from, subscribeRequest, connection)
        }
    }

    private fun handleSubscription(
        from: Jid, subscribeRequest: Presence, xmppConnection: XMPPConnection
    ): SubscribeListener.SubscribeAnswer {
        scope.launch {
            kotlin.runCatching {
                if (subscribeRequest.type == Presence.Type.subscribe) {
                    val subscription =
                        subscriptionRepository.getSubscription(jid = from.asBareJid().toString())
                    if (subscription == null || subscription.isPending) {
                        val vcardManager = VCardManager.getInstanceFor(xmppConnection)
                        val vCard =
                            vcardManager.loadVCard(from.asBareJid().asEntityBareJidOrThrow())
                        if (vCard.emailHome.isNullOrEmpty().not()) {
                            fcmApi.sendFcmMessage(
                                FcmMessage(
                                    vCard.emailHome,
                                    FcmNotification(
                                        title = "Subscription request",
                                        body = "from:${from.localpartOrNull}"
                                    )
                                )
                            )
                        }
                        subscriptionRepository.updateSubscription(
                            listOf(
                                Subscription(
                                    subscriptionFrom = from.asBareJid().toString(),
                                    isApproved = false
                                )
                            )
                        )
                    }

                }
            }
        }
        return SubscribeListener.SubscribeAnswer.Approve
    }
}