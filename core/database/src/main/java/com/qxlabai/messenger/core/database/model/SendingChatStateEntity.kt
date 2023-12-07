package com.qxlabai.messenger.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.qxlabai.messenger.core.model.data.ChatState
import com.qxlabai.messenger.core.model.data.SendingChatState

@Entity(
    tableName = "sending_chat_state",
)
data class SendingChatStateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "peer_jid")
    val peerJid: String,
    @ColumnInfo(name = "chat_state")
    val chatState: ChatState,
    val consumed: Boolean
)

fun SendingChatStateEntity.asExternalModel() = SendingChatState(
    id = id,
    peerJid = peerJid,
    chatState = chatState,
    consumed = consumed
)
