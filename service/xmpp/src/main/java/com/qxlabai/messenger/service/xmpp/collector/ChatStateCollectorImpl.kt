package com.qxlabai.messenger.service.xmpp.collector

import com.qxlabai.messenger.core.model.data.SendingChatState
import com.qxlabai.messenger.core.data.repository.SendingChatStatesRepository
import javax.inject.Inject

class ChatStateCollectorImpl @Inject constructor(
    private val sendingChatStatesRepository: SendingChatStatesRepository
) : ChatStateCollector {

    override suspend fun collectChatState(
        onChatStateChanged: suspend (SendingChatState) -> Unit
    ) {
        sendingChatStatesRepository.getSendingChatStatesStream().collect { states ->
            states.forEach { sendingChatState ->
                sendingChatStatesRepository.updateSendingChatState(
                    sendingChatState.copy(consumed = true)
                )
                onChatStateChanged(sendingChatState)
            }
        }
    }
}
