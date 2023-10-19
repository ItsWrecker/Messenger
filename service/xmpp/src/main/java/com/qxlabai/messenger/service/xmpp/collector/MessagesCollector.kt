package com.qxlabai.messenger.service.xmpp.collector

import com.qxlabai.messenger.core.model.data.Message

interface MessagesCollector {
    /**
     * Collects the changes to messages stream which should be send
     * by XMPP, originated from database
     * */
    suspend fun collectShouldSendMessages(
        sendMessages: suspend (List<Message>) -> Unit
    )
}
