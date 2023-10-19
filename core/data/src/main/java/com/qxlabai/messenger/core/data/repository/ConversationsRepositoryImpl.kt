package com.qxlabai.messenger.core.data.repository

import com.qxlabai.messenger.core.model.data.ChatState
import com.qxlabai.messenger.core.model.data.Conversation
import com.qxlabai.messenger.core.data.model.asEntity
import com.qxlabai.messenger.core.database.dao.ConversationDao
import com.qxlabai.messenger.core.database.model.PopulatedConversation
import com.qxlabai.messenger.core.database.model.asExternalModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ConversationsRepositoryImpl @Inject constructor(
    private val conversationDao: ConversationDao
) : ConversationsRepository {

    override fun getConversation(peerJid: String): Flow<Conversation?> =
        conversationDao.getConversationEntity(peerJid).map { it?.asExternalModel() }

    override fun getConversationsStream(): Flow<List<Conversation>> =
        conversationDao.getConversationEntitiesStream()
            .map { populatedConversations ->
                populatedConversations
                    .sortedByDescending { it.lastMessage?.sendTime }
                    .map(PopulatedConversation::asExternalModel)
            }

    override suspend fun addConversation(conversation: Conversation): Long =
        conversationDao.insert(conversation.asEntity())

    override suspend fun updateConversation(
        peerJid: String,
        unreadMessagesCount: Int,
        chatState: ChatState,
        lastMessageId: Long
    ) = conversationDao.update(
        peerJid = peerJid,
        unreadMessagesCount = unreadMessagesCount,
        chatState = chatState,
        lastMessageId = lastMessageId
    )

    override suspend fun updateConversation(
        peerJid: String,
        unreadMessagesCount: Int,
        isChatOpen: Boolean
    ) = conversationDao.update(
        peerJid = peerJid,
        unreadMessagesCount = unreadMessagesCount,
        isChatOpen = isChatOpen
    )

    override suspend fun updateConversation(peerJid: String, lastMessageId: Long) =
        conversationDao.update(peerJid = peerJid, lastMessageId = lastMessageId)

    override suspend fun updateConversation(peerJid: String, chatState: ChatState) =
        conversationDao.update(peerJid = peerJid, chatState = chatState)

    override suspend fun updateConversation(peerJid: String, draftMessage: String?) =
        conversationDao.update(peerJid = peerJid, draftMessage = draftMessage)

    override suspend fun updateConversation(peerJid: String, isChatOpen: Boolean) =
        conversationDao.update(peerJid = peerJid, isChatOpen = isChatOpen)
}
