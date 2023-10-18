package com.qxlabai.presentation.chat

import com.qxlabai.domain.entity.Conversation
import com.qxlabai.domain.entity.Message
import com.qxlabai.presentation.core.State
import kotlinx.datetime.LocalDate

sealed class ChatUiState(val contactId: String): State {
    class Success(
        contactId: String,
        val conversation: Conversation,
        val messagesBySendTime: Map<LocalDate, List<Message>>
    ) : ChatUiState(contactId)

    class Loading(contactId: String) : ChatUiState(contactId)
}