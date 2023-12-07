package com.qxlabai.messenger.core.data.repository

import com.qxlabai.messenger.core.model.data.Conversation
import com.qxlabai.messenger.core.model.data.Message
import com.qxlabai.messenger.core.model.data.MessageStatus
import com.qxlabai.messenger.core.data.model.asEntity
import com.qxlabai.messenger.core.database.dao.MessageDao
import com.qxlabai.messenger.core.database.model.MessageEntity
import com.qxlabai.messenger.core.database.model.asExternalModel
import com.qxlabai.messenger.core.database.transaction.MessageTransaction
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MessagesRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao,
    private val messageTransaction: MessageTransaction
) : MessagesRepository {

    override fun getMessage(id: String): Flow<Message> =
        messageDao.getMessageEntity(id).map(MessageEntity::asExternalModel)

    override fun getMessageByStanzaId(stanzaId: String): Flow<Message?> =
        messageDao.getMessageEntityByStanzaId(stanzaId).map { it?.asExternalModel() }

    override fun getMessagesStream(): Flow<List<Message>> =
        messageDao.getMessageEntitiesStream()
            .map { it.map(MessageEntity::asExternalModel) }

    override fun getMessagesStream(peerJid: String): Flow<List<Message>> =
        messageDao.getMessageEntitiesStream(peerJid)
            .map { it.map(MessageEntity::asExternalModel) }

    override fun getMessagesStream(ids: Set<String>): Flow<List<Message>> =
        messageDao.getMessageEntitiesStream(ids)
            .map { it.map(MessageEntity::asExternalModel) }

    override fun getMessagesStream(status: MessageStatus): Flow<List<Message>> =
        messageDao.getMessageEntitiesStream(status)
            .map { it.map(MessageEntity::asExternalModel) }

    override suspend fun addMessage(message: Message): Long =
        messageDao.insert(message.asEntity())

    override suspend fun updateMessage(message: Message) =
        messageDao.upsert(message.asEntity())

    override suspend fun updateMessages(messages: List<Message>) =
        messageDao.upsert(messages.map(Message::asEntity))

    override suspend fun deleteMessage(id: String) =
        messageDao.deleteMessage(id)

    override suspend fun handleIncomingMessage(
        message: Message,
        maybeNewConversation: Conversation
    ) = messageTransaction.handleIncomingMessage(
        message.asEntity(),
        maybeNewConversation.asEntity()
    )

    override suspend fun handleOutgoingMessage(stanzaId: String) =
        messageTransaction.handleOutgoingMessage(stanzaId)
}
