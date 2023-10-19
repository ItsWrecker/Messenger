package com.qxlabai.messenger.service.xmpp.collector

import com.qxlabai.messenger.core.model.data.SendingChatState

interface ChatStateCollector {

    suspend fun collectChatState(onChatStateChanged: suspend (SendingChatState) -> Unit)
}
