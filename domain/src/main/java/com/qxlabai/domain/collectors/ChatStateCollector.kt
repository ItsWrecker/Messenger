package com.qxlabai.domain.collectors

import com.qxlabai.domain.entity.SendingChatState

interface ChatStateCollector {

    suspend fun collectChatState(onChatStateChanged: suspend (SendingChatState) -> Unit)
}
