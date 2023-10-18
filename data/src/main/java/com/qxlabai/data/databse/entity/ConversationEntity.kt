package com.qxlabai.data.databse.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.qxlabai.domain.entity.ChatState


@Entity(
    tableName = "conversations",
    foreignKeys = [
        ForeignKey(
            entity = MessageEntity::class,
            parentColumns = ["id"],
            childColumns = ["last_message_id"],
            onDelete = ForeignKey.NO_ACTION
        ),
    ],
    indices = [
        Index(value = ["last_message_id"])
    ]
)
data class ConversationEntity(
    @PrimaryKey
    @ColumnInfo(name = "peer_jid")
    val peerJid: String,
    @ColumnInfo(name = "draft_message")
    val draftMessage: String?,
    @ColumnInfo(name = "last_message_id")
    val lastMessageId: Long?,
    @ColumnInfo(name = "unread_messages_count")
    val unreadMessagesCount: Int,
    @ColumnInfo(name = "chat_state")
    val chatState: ChatState,
    @ColumnInfo(name = "is_chat_open")
    val isChatOpen: Boolean
)
