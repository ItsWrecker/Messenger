package com.qxlabai.messenger.service.xmpp.collector

import com.qxlabai.messenger.core.data.repository.ContactsRepository
import com.qxlabai.messenger.core.data.repository.SubscriptionRepository
import com.qxlabai.messenger.core.model.data.Contact
import com.qxlabai.messenger.core.model.data.Subscription
import com.qxlabai.messenger.service.xmpp.notification.NotificationManager
import javax.inject.Inject

class SubscriptionCollectorImpl @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository,
    private val contactsRepository: ContactsRepository,
    private val notificationManager: NotificationManager
) : SubscriptionCollector {

    override suspend fun collectIsApproved() {
        subscriptionRepository.isApproved().collect { subscriptions ->
            subscriptions.forEach { subscription ->
                if (subscription.isPending.not()) {
                    contactsRepository.updateContacts(
                        listOf(
                            Contact.createWithSubscription(jid = subscription.subscriptionFrom)
                        )
                    )
                    notificationManager.removeNotification(100)
                }
            }
        }
    }
}