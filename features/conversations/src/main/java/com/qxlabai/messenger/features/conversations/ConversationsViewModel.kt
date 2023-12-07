package com.qxlabai.messenger.features.conversations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.qxlabai.messenger.core.model.data.Conversation
import com.qxlabai.messenger.core.data.repository.ConversationsRepository
import com.qxlabai.messenger.features.conversations.ConversationsUiState.Loading
import com.qxlabai.messenger.features.conversations.ConversationsUiState.Success
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class ConversationsViewModel @Inject constructor(
    conversationsRepository: ConversationsRepository
) : ViewModel() {

    val uiState: StateFlow<ConversationsUiState> =
        conversationsRepository.getConversationsStream()
            .map { conversations ->
                Success(conversations)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = Loading
            )
}

sealed interface ConversationsUiState {
    object Loading : ConversationsUiState

    data class Success(val conversations: List<Conversation>) : ConversationsUiState
}
