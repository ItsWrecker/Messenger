package com.qxlabai.messenger.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.qxlabai.messenger.R
import com.qxlabai.messenger.conversation.ContactThumb
import com.qxlabai.presentation.chat.ChatUiState

@Composable
fun TopAppBarTitle(
    uiState: ChatUiState
) {
    when (uiState) {
        is ChatUiState.Loading -> {
            Text(text = "Chat")
        }

        is ChatUiState.Success -> {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ContactThumb(
                    firstLetter = uiState.conversation.firstLetter,
                    smallShape = true
                )
                Text(
                    text = uiState.conversation.peerLocalPart,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

    }
}