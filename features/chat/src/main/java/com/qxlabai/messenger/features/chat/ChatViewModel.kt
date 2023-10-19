package com.qxlabai.messenger.features.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.qxlabai.messenger.core.model.data.ChatState
import com.qxlabai.messenger.core.model.data.ChatState.Active
import com.qxlabai.messenger.core.model.data.ChatState.Composing
import com.qxlabai.messenger.core.model.data.ChatState.Paused
import com.qxlabai.messenger.core.model.data.Conversation
import com.qxlabai.messenger.core.model.data.Message
import com.qxlabai.messenger.core.model.data.SendingChatState
import com.qxlabai.messenger.core.common.utils.localDate
import com.qxlabai.messenger.core.data.repository.ConversationsRepository
import com.qxlabai.messenger.core.data.repository.MessagesRepository
import com.qxlabai.messenger.core.data.repository.SendingChatStatesRepository
import com.qxlabai.messenger.features.chat.ChatUiState.Loading
import com.qxlabai.messenger.features.chat.ChatUiState.Success
import com.qxlabai.messenger.features.chat.navigation.ChatDestination
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val conversationsRepository: ConversationsRepository,
    private val messagesRepository: MessagesRepository,
    private val sendingChatStatesRepository: SendingChatStatesRepository
) : ViewModel() {

    private val contactId: String = checkNotNull(
        savedStateHandle[ChatDestination.contactJidArg]
    )

    private val conversation = conversationsRepository.getConversation(peerJid = contactId)

    private val messages = messagesRepository.getMessagesStream(peerJid = contactId)

    private var currentChatState = CurrentChatState(Active)

    init {
        viewModelScope.launch {
            openChat()
            sendChatState(Active)
        }
    }

    val uiState: StateFlow<ChatUiState> =
        combine(
            conversation,
            messages
        ) { conversation, messages ->
            if (conversation != null) {
                val messagesBySendTime =
                    messages.groupBy { it.sendTime.localDate }
                        .toSortedMap(Comparator.reverseOrder())

                Success(contactId, conversation, messagesBySendTime)
            } else {
                conversationsRepository.addConversation(
                    Conversation.createNewConversation(peerJid = contactId)
                )
                Loading(contactId)
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = Loading(contactId)
            )

    fun sendMessage(text: String) {
        currentChatState.cancelSendingPausedState()
        viewModelScope.launch {
            messagesRepository.addMessage(
                Message.createNewMessage(text, contactId)
            )

            updateDraft(null)
        }
    }

    fun userTyping(messageText: String) {
        currentChatState.cancelSendingPausedState()
        val sendingPausedStateJob = viewModelScope.launch {
            delay(3_000)
            sendChatState(Paused)
        }
        currentChatState = currentChatState.copy(sendingPausedStateJob = sendingPausedStateJob)

        viewModelScope.launch {
            if (currentChatState.shouldSendComposing()) {
                sendChatState(Composing)
            }

            updateDraft(messageText)
        }
    }

    private suspend fun sendChatState(chatState: ChatState) {
        currentChatState = currentChatState.copy(chatState = chatState)
        sendingChatStatesRepository.updateSendingChatState(
            SendingChatState(peerJid = contactId, chatState = chatState)
        )
    }

    private suspend fun openChat() {
        conversationsRepository.updateConversation(
            peerJid = contactId,
            unreadMessagesCount = 0,
            isChatOpen = true
        )
    }

    private suspend fun updateDraft(messageText: String?) {
        val updatedDraft = if (messageText?.isNotBlank() == true) messageText else null
        conversationsRepository.updateConversation(
            peerJid = contactId,
            draftMessage = updatedDraft
        )
    }
}

data class CurrentChatState(
    val chatState: ChatState,
    val sendingPausedStateJob: Job? = null
) {
    fun cancelSendingPausedState() {
        sendingPausedStateJob?.cancel()
    }

    fun shouldSendComposing() = chatState != Composing
}

sealed class ChatUiState(val contactId: String) {
    class Success(
        contactId: String,
        val conversation: Conversation,
        val messagesBySendTime: Map<LocalDate, List<Message>>
    ) : ChatUiState(contactId)

    class Loading(contactId: String) : ChatUiState(contactId)
}

val Success.shouldShowChatState: Boolean
    get() = conversation.chatState == Composing || conversation.chatState == Paused
