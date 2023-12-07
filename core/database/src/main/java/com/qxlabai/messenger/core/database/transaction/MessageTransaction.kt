package com.qxlabai.messenger.core.database.transaction

import com.qxlabai.messenger.core.database.model.ConversationEntity
import com.qxlabai.messenger.core.database.model.MessageEntity

interface MessageTransaction {

    suspend fun handleIncomingMessage(
        messageEntity: MessageEntity,
        maybeNewConversationEntity: ConversationEntity
    )

    suspend fun handleOutgoingMessage(stanzaId: String)
}
