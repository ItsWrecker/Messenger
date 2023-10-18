package com.qxlabai.messenger.chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.qxlabai.presentation.chat.ChatUiState
import com.qxlabai.presentation.chat.ChatViewModel
import kotlin.reflect.KFunction1

@Composable
fun ChatRoute(
    onBackClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    ChatScreen(
        uiState = uiState,
        sendMessage = viewModel::sendMessage,
        onUserTyping = viewModel::userTyping,
        onBackClick = onBackClick,
        modifier = modifier
    )
}
