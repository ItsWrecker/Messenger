package com.qxlabai.data.datastore.repository

import com.qxlabai.data.databse.dao.ConversationDao
import com.qxlabai.data.databse.entity.PopulatedConversation
import com.qxlabai.data.databse.entity.asExternalModel
import com.qxlabai.data.datastore.entiry.asEntity
import com.qxlabai.domain.entity.ChatState
import com.qxlabai.domain.entity.Conversation
import com.qxlabai.domain.repositories.ConversationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ConversationRepositoryImpl @Inject constructor(
    private val conversation: ConversationDao
) : ConversationsRepository {

    override fun getConversation(peerJid: String): Flow<Conversation?> =
        conversation.getConversationEntity(peerJid).map { it?.asExternalModel() }


    override fun getConversationsStream(): Flow<List<Conversation>> =
        conversation.getConversationEntitiesStream()
            .map { populatedConversations ->
                populatedConversations
                    .sortedByDescending { it.lastMessage?.sendTime }
                    .map(PopulatedConversation::asExternalModel)
            }


    override suspend fun addConversation(conversation: Conversation): Long =
        this.conversation.insert(conversation.asEntity())


    override suspend fun updateConversation(
        peerJid: String,
        unreadMessagesCount: Int,
        chatState: ChatState,
        lastMessageId: Long
    ) = conversation.update(
        peerJid = peerJid,
        unreadMessagesCount = unreadMessagesCount,
        chatState = chatState,
        lastMessageId = lastMessageId
    )

    override suspend fun updateConversation(
        peerJid: String,
        unreadMessagesCount: Int,
        isChatOpen: Boolean
    ) = conversation.update(
        peerJid = peerJid,
        unreadMessagesCount = unreadMessagesCount,
        isChatOpen = isChatOpen
    )

    override suspend fun updateConversation(peerJid: String, lastMessageId: Long) =
        conversation.update(peerJid = peerJid, lastMessageId = lastMessageId)

    override suspend fun updateConversation(peerJid: String, chatState: ChatState) =
        conversation.update(peerJid = peerJid, chatState = chatState)

    override suspend fun updateConversation(peerJid: String, draftMessage: String?) =
        conversation.update(peerJid = peerJid, draftMessage = draftMessage)

    override suspend fun updateConversation(peerJid: String, isChatOpen: Boolean) =
        conversation.update(peerJid = peerJid, isChatOpen = isChatOpen)

}