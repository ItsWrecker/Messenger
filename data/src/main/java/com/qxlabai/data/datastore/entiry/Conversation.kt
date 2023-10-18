package com.qxlabai.data.datastore.entiry

import com.qxlabai.data.databse.entity.ConversationEntity
import com.qxlabai.domain.entity.Conversation

fun Conversation.asEntity() = ConversationEntity(
    peerJid = peerJid,
    draftMessage = draftMessage,
    lastMessageId = lastMessage?.id,
    unreadMessagesCount = unreadMessagesCount,
    chatState = chatState,
    isChatOpen = isChatOpen
)
