package com.qxlabai.messenger.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.qxlabai.messenger.core.model.data.Subscription


@Entity(tableName = "subscriptions")
data class SubscriptionEntity(
    @PrimaryKey
    @ColumnInfo(name = "subscription_from")
    val subscriptionFrom: String,

    @ColumnInfo(name = "is_approved")
    val isApproved: Boolean,

    @ColumnInfo(name = "is_declined")
    val isDeclined: Boolean,

    @ColumnInfo(name = "is_pending")
    val isPending: Boolean
)

fun SubscriptionEntity.asExternalModel() = Subscription(
    subscriptionFrom = subscriptionFrom,
    isApproved = isApproved,
    isDeclined = isPending,
    isPending = isPending
)