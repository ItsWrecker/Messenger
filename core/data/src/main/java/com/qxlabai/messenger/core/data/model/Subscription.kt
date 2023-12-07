package com.qxlabai.messenger.core.data.model

import com.qxlabai.messenger.core.database.model.SubscriptionEntity
import com.qxlabai.messenger.core.model.data.Subscription

fun Subscription.asEntity() = SubscriptionEntity(
    subscriptionFrom, isApproved, isDeclined, isPending
)