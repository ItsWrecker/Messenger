package com.qxlabai.presentation.massenger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qxlabai.domain.collectors.SendingChatStatesRepository
import com.qxlabai.domain.entity.ChatState
import com.qxlabai.domain.entity.SendingChatState
import com.qxlabai.domain.repositories.ConversationsRepository
import com.qxlabai.domain.repositories.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessengerViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val conversationsRepository: ConversationsRepository,
    private val sendingChatStatesRepository: SendingChatStatesRepository
) : ViewModel() {

    val uiState: StateFlow<MessengerUiState> = preferencesRepository.getConnectionStatus()
        .map { connectionStatus ->
            if (connectionStatus.availability && connectionStatus.authenticated) {
                MessengerUiState.Connected
            } else {
                MessengerUiState.Connecting
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MessengerUiState.Idle
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
            SendingChatState(peerJid = contactId, chatState = ChatState.Inactive)
        )
    }


}