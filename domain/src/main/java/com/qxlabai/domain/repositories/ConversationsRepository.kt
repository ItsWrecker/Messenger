package com.qxlabai.domain.repositories

import com.qxlabai.domain.entity.ChatState
import com.qxlabai.domain.entity.Conversation
import kotlinx.coroutines.flow.Flow

interface ConversationsRepository {


    fun getConversation(peerJid: String): Flow<Conversation?>

    fun getConversationsStream(): Flow<List<Conversation>>

    suspend fun addConversation(conversation: Conversation): Long

    suspend fun updateConversation(
        peerJid: String,
        unreadMessagesCount: Int,
        chatState: ChatState,
        lastMessageId: Long
    )

    suspend fun updateConversation(
        peerJid: String,
        unreadMessagesCount: Int,
        isChatOpen: Boolean
    )

    suspend fun updateConversation(peerJid: String, lastMessageId: Long)

    suspend fun updateConversation(peerJid: String, chatState: ChatState)

    suspend fun updateConversation(peerJid: String, draftMessage: String?)

    suspend fun updateConversation(peerJid: String, isChatOpen: Boolean)
}