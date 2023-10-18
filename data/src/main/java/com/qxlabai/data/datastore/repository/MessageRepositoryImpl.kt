package com.qxlabai.data.datastore.repository

import com.qxlabai.data.databse.dao.MessageDao
import com.qxlabai.data.databse.entity.MessageEntity
import com.qxlabai.data.databse.entity.asExternalModel
import com.qxlabai.data.databse.transaction.MessageTransaction
import com.qxlabai.data.datastore.entiry.asEntity
import com.qxlabai.domain.collectors.MessagesRepository
import com.qxlabai.domain.entity.Conversation
import com.qxlabai.domain.entity.Message
import com.qxlabai.domain.entity.MessageStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MessageRepositoryImpl @Inject  constructor(
    private val messageDao: MessageDao,
    private val transaction: MessageTransaction
): MessagesRepository{

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
    ) = transaction.handleIncomingMessage(
        message.asEntity(),
        maybeNewConversation.asEntity()
    )

    override suspend fun handleOutgoingMessage(stanzaId: String) =
        transaction.handleOutgoingMessage(stanzaId)
}