package com.qxlabai.data.databse.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.qxlabai.domain.entity.Message
import com.qxlabai.domain.entity.MessageStatus
import kotlinx.datetime.Instant

@Entity(
    tableName = "messages",
)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "stanza_id")
    val stanzaId: String,
    @ColumnInfo(name = "peer_jid")
    val peerJid: String,
    val body: String,
    @ColumnInfo(name = "send_time")
    val sendTime: Instant,
    val status: MessageStatus
)

fun MessageEntity.asExternalModel() = Message(
    id = id,
    stanzaId = stanzaId,
    peerJid = peerJid,
    body = body,
    sendTime = sendTime,
    status = status
)
