package com.qxlabai.messenger.core.data.repository

import com.qxlabai.messenger.core.data.model.asEntity
import com.qxlabai.messenger.core.database.dao.SubscriptionDao
import com.qxlabai.messenger.core.database.model.SubscriptionEntity
import com.qxlabai.messenger.core.database.model.asExternalModel
import com.qxlabai.messenger.core.model.data.Subscription
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SubscriptionRepositoryImpl @Inject constructor(
    private val subscriptionDao: SubscriptionDao
) : SubscriptionRepository {


    override fun getSubscriptionStream(): Flow<List<Subscription>> {
        return subscriptionDao.getSubscriptionStream().map {
            it.map(SubscriptionEntity::asExternalModel)
        }
    }

    override suspend fun updateSubscription(subscription: List<Subscription>) {
        subscriptionDao.upsert(subscription.map { it.asEntity() })
    }

    override fun isApproved(): Flow<List<Subscription>> {
        return subscriptionDao.getApprovedStream().map {
            it.map(SubscriptionEntity::asExternalModel)
        }
    }

    override fun getSubscription(jid: String): Subscription? {
        return subscriptionDao.getSubscription(jid)?.asExternalModel()
    }
}