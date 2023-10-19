package com.qxlabai.messenger.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.qxlabai.messenger.core.model.data.Conversation


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
