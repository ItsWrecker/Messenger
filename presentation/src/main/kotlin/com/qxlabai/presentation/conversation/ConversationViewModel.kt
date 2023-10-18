package com.qxlabai.presentation.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qxlabai.domain.repositories.ConversationsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class ConversationViewModel @Inject constructor(
    private val conversationsRepository: ConversationsRepository
) : ViewModel() {

    val uiState: StateFlow<ConversationUiState> = conversationsRepository.getConversationsStream()
        .map { conversations ->
            ConversationUiState.Success(conversations)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ConversationUiState.Loading
        )
}