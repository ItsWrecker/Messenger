package com.qxlabai.messenger.conversation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.qxlabai.presentation.conversation.ConversationViewModel


@Composable
fun ConversationRoute(
    navigateToChat: (String) -> Unit,
    modifier: Modifier,
    viewModel: ConversationViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()




}
