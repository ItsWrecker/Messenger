package com.qxlabai.presentation.chat

import com.qxlabai.domain.entity.ChatState
import kotlinx.coroutines.Job

data class CurrentChatState(
    val chatState: ChatState,
    val sendingPausedStateJob: Job? = null
) {
    fun cancelSendingPausedState() {
        sendingPausedStateJob?.cancel()
    }

    fun shouldSendComposing() = chatState != ChatState.Composing
}