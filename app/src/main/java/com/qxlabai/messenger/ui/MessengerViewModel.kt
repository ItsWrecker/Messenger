package com.qxlabai.messenger.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.qxlabai.messenger.core.model.data.ChatState.Inactive
import com.qxlabai.messenger.core.model.data.SendingChatState
import com.qxlabai.messenger.core.data.repository.ConversationsRepository
import com.qxlabai.messenger.core.data.repository.PreferencesRepository
import com.qxlabai.messenger.core.data.repository.SendingChatStatesRepository
import com.qxlabai.messenger.ui.ConnectionStatusUiState.Connected
import com.qxlabai.messenger.ui.ConnectionStatusUiState.Connecting
import com.qxlabai.messenger.ui.ConnectionStatusUiState.Idle
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class MessengerViewModel @Inject constructor(
    preferencesRepository: PreferencesRepository,
    private val conversationsRepository: ConversationsRepository,
    private val sendingChatStatesRepository: SendingChatStatesRepository
) : ViewModel() {

    val connectionStatusUiState: StateFlow<ConnectionStatusUiState> =
        preferencesRepository.getConnectionStatus()
            .map { connectionStatus ->
                if (connectionStatus.availability && connectionStatus.authenticated) {
                    Connected
                } else {
                    // TODO: navigate to auth screen if connection available but not authenticated
                    Connecting
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = Idle
            )

    fun onExitChat(contactId: String) {
        viewModelScope.launch {
            closeConversation(contactId)
            resetChatState(contactId)
        }
    }

    private suspend fun closeConversation(contactId: String) {
        conversationsRepository.updateConversation(
            peerJid = contactId,
            isChatOpen = false
        )
    }

    private suspend fun resetChatState(contactId: String) {
        sendingChatStatesRepository.updateSendingChatState(
            SendingChatState(peerJid = contactId, chatState = Inactive)
        )
    }
}

sealed interface ConnectionStatusUiState {
    object Idle : ConnectionStatusUiState

    object Connected : ConnectionStatusUiState

    object Connecting : ConnectionStatusUiState
}
