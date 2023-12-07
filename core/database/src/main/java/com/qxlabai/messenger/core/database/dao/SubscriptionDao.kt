package com.qxlabai.messenger.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.qxlabai.messenger.core.database.model.SubscriptionEntity
import com.qxlabai.messenger.core.model.data.Subscription
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriptionDao {

    @Query(value = "SELECT * FROM subscriptions WHERE is_approved = 0 AND is_declined = 0")
    fun getSubscriptionStream(): Flow<List<SubscriptionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(subscriptionEntity: List<SubscriptionEntity>)

    @Query(
        value = "SELECT * FROM subscriptions WHERE is_approved = 1 AND is_declined = 0"
    )
    fun getApprovedStream(): Flow<List<SubscriptionEntity>>

    @Query(value = "SELECT * FROM subscriptions WHERE subscription_from=:jid")
    fun getSubscription(jid: String): SubscriptionEntity?

}