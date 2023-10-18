package com.qxlabai.presentation.conversation

import com.qxlabai.domain.entity.Conversation
import com.qxlabai.presentation.core.State


sealed interface ConversationUiState : State {
    object Loading: ConversationUiState

    data class Success(
        val conversations: List<Conversation>
    ): ConversationUiState
}