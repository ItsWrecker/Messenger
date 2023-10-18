package com.qxlabai.domain.collectors

import com.qxlabai.domain.entity.Message


interface MessagesCollector {
    /**
     * Collects the changes to messages stream which should be send
     * by XMPP, originated from database
     * */
    suspend fun collectShouldSendMessages(
        sendMessages: suspend (List<Message>) -> Unit
    )
}
