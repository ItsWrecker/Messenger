package com.qxlabai.messenger.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.qxlabai.messenger.core.model.data.Contact
import com.qxlabai.messenger.core.model.data.Presence
import kotlinx.datetime.Instant

@Entity(
    tableName = "contacts",
)
data class ContactEntity(
    @PrimaryKey
    val jid: String,
    @ColumnInfo(name = "presence_type")
    val presenceType: Presence.Type,
    @ColumnInfo(name = "presence_mode")
    val presenceMode: Presence.Mode,
    @ColumnInfo(name = "presence_status")
    val presenceStatus: String,
    @ColumnInfo(name = "presence_priority")
    val presencePriority: Int,
    @ColumnInfo(name = "last_time")
    val lastTime: Instant,
    @ColumnInfo(name = "should_add_to_roster")
    val shouldAddToRoster: Boolean,
    @ColumnInfo(name = "is_approved")
    val isApproved: Boolean
)

fun ContactEntity.asExternalModel() = Contact(
    jid = jid,
    presence = Presence(
        type = presenceType,
        mode = presenceMode,
        status = presenceStatus,
        priority = presencePriority
    ),
    lastTime = lastTime,
    shouldAddToRoster = shouldAddToRoster,
    subscribed = if (isApproved) Contact.Subscribed.APPROVED else Contact.Subscribed.PENDING
)
