package com.qxlabai.presentation.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qxlabai.domain.collectors.MessagesRepository
import com.qxlabai.domain.collectors.SendingChatStatesRepository
import com.qxlabai.domain.core.localDate
import com.qxlabai.domain.entity.ChatState
import com.qxlabai.domain.entity.Conversation
import com.qxlabai.domain.entity.Message
import com.qxlabai.domain.entity.SendingChatState
import com.qxlabai.domain.repositories.ConversationsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    private var currentChatState = CurrentChatState(ChatState.Active)

    init {
        viewModelScope.launch {
            openChat()
            sendChatState(ChatState.Active)
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

                ChatUiState.Success(contactId, conversation, messagesBySendTime)
            } else {
                conversationsRepository.addConversation(
                    Conversation.createNewConversation(peerJid = contactId)
                )
                ChatUiState.Loading(contactId)
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ChatUiState.Loading(contactId)
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
            sendChatState(ChatState.Paused)
        }
        currentChatState = currentChatState.copy(sendingPausedStateJob = sendingPausedStateJob)

        viewModelScope.launch {
            if (currentChatState.shouldSendComposing()) {
                sendChatState(ChatState.Composing)
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

val ChatUiState.Success.shouldShowChatState: Boolean
    get() = conversation.chatState == ChatState.Composing || conversation.chatState == ChatState.Paused
