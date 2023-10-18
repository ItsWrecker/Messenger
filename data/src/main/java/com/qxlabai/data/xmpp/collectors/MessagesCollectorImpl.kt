package com.qxlabai.data.xmpp.collectors

import com.qxlabai.domain.collectors.MessagesCollector
import com.qxlabai.domain.collectors.MessagesRepository
import com.qxlabai.domain.entity.Message
import com.qxlabai.domain.entity.MessageStatus
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class MessagesCollectorImpl @Inject constructor(
    private val messagesRepository: MessagesRepository
) : MessagesCollector {

    override suspend fun collectShouldSendMessages(sendMessages: suspend (List<Message>) -> Unit) {
        messagesRepository.getMessagesStream(status = MessageStatus.ShouldSend)
            .collectLatest { messages ->
                val updatedMessages = messages.map { it.withStatus(status = MessageStatus.Sending) }
                messagesRepository.updateMessages(updatedMessages)
                sendMessages(updatedMessages)
            }
    }
}