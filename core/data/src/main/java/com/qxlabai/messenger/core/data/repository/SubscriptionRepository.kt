package com.qxlabai.messenger.core.data.repository

import com.qxlabai.messenger.core.model.data.Subscription
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {

    fun getSubscriptionStream(): Flow<List<Subscription>>

    suspend fun updateSubscription(subscription: List<Subscription>)

    fun isApproved(): Flow<List<Subscription>>

    fun getSubscription(jid: String): Subscription?
}