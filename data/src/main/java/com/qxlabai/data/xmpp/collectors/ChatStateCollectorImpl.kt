package com.qxlabai.data.xmpp.collectors

import com.qxlabai.domain.collectors.ChatStateCollector
import com.qxlabai.domain.collectors.SendingChatStatesRepository
import com.qxlabai.domain.entity.SendingChatState
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class ChatStateCollectorImpl @Inject constructor(
    private val sendingChatState: SendingChatStatesRepository
) : ChatStateCollector {

    override suspend fun collectChatState(onChatStateChanged: suspend (SendingChatState) -> Unit) {
        sendingChatState.getSendingChatStatesStream().collectLatest { state ->
            state.forEach {
                sendingChatState.updateSendingChatState(it.copy(consumed = true))
                onChatStateChanged(it)
            }
        }
    }
}