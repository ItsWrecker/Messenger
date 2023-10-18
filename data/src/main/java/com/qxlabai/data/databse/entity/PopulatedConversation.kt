package com.qxlabai.data.databse.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.qxlabai.domain.entity.Conversation

/**
 * External data layer representation of a fully populated conversation
 */
data class PopulatedConversation(
    @Embedded
    val entity: ConversationEntity,
    @Relation(
        parentColumn = "last_message_id",
        entityColumn = "id"
    )
    val lastMessage: MessageEntity?
)

fun PopulatedConversation.asExternalModel() = Conversation(
    peerJid = entity.peerJid,
    draftMessage = entity.draftMessage,
    lastMessage = lastMessage?.asExternalModel(),
    unreadMessagesCount = entity.unreadMessagesCount,
    chatState = entity.chatState,
    isChatOpen = entity.isChatOpen
)
