package com.qxlabai.messenger.service.xmpp.collector

import com.qxlabai.messenger.core.model.data.Message
import com.qxlabai.messenger.core.model.data.MessageStatus
import com.qxlabai.messenger.core.model.data.MessageStatus.Sending
import com.qxlabai.messenger.core.data.repository.MessagesRepository
import javax.inject.Inject

class MessagesCollectorImpl @Inject constructor(
    private val messagesRepository: MessagesRepository
) : MessagesCollector {

    override suspend fun collectShouldSendMessages(sendMessages: suspend (List<Message>) -> Unit) {
        messagesRepository.getMessagesStream(status = MessageStatus.ShouldSend)
            .collect { messages ->
                val updatedMessages = messages.map { it.withStatus(status = Sending) }
                messagesRepository.updateMessages(updatedMessages)
                sendMessages(updatedMessages)
            }
    }
}
