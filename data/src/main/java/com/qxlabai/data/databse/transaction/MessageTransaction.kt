package com.qxlabai.data.databse.transaction

import com.qxlabai.data.databse.entity.ConversationEntity
import com.qxlabai.data.databse.entity.MessageEntity


interface MessageTransaction {

    suspend fun handleIncomingMessage(
        messageEntity: MessageEntity,
        maybeNewConversationEntity: ConversationEntity
    )

    suspend fun handleOutgoingMessage(stanzaId: String)
}
