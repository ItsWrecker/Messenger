package com.qxlabai.messenger.service.xmpp.collector

import com.qxlabai.messenger.core.model.data.Contact
import com.qxlabai.messenger.core.model.data.Subscription

interface SubscriptionCollector {

    suspend fun collectIsApproved()
}