package com.qxlabai.messenger.core.data.model

import com.qxlabai.messenger.core.model.data.Conversation
import com.qxlabai.messenger.core.database.model.ConversationEntity

fun Conversation.asEntity() = ConversationEntity(
    peerJid = peerJid,
    draftMessage = draftMessage,
    lastMessageId = lastMessage?.id,
    unreadMessagesCount = unreadMessagesCount,
    chatState = chatState,
    isChatOpen = isChatOpen
)
