package com.qxlabai.messenger.chat

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.qxlabai.domain.core.formatted
import com.qxlabai.domain.core.localTime
import com.qxlabai.domain.entity.ChatState
import com.qxlabai.domain.entity.Message
import com.qxlabai.domain.entity.MessageStatus
import com.qxlabai.presentation.chat.ChatUiState
import com.qxlabai.presentation.chat.shouldShowChatState
import kotlinx.datetime.LocalDate


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ChatScreen(
    uiState: ChatUiState,
    sendMessage: (String) -> Unit,
    onUserTyping: (String) -> Unit,
    onBackClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val keyboardState by KeyboardAsState()

    if (uiState is ChatUiState.Success) {
        LaunchedEffect(key1 = uiState.messagesBySendTime.values.flatten().size) {
            scrollState.animateScrollToItem(0)
        }
    }
    LaunchedEffect(key1 = keyboardState) {
        if (keyboardState == KeyboardState.Closed) {
            focusManager.clearFocus()
        }
    }

    Scaffold(modifier = modifier, topBar = {
        TopAppBar(title = { TopAppBarTitle(uiState = uiState) })
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            LazyColumn(reverseLayout = true,
                state = scrollState,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
                contentPadding = PaddingValues(all = 16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .pointerInput(Unit) {
                        detectTapGestures {
                            if (keyboardState == KeyboardState.Opened) {
                                focusManager.clearFocus()
                            }
                        }
                    }) {
                messages(uiState = uiState)
            }
            ChatInput(uiState = uiState, onUserTyping = onUserTyping, sendMessage = sendMessage)
        }
    }

    BackHandler {
        onBackClick(uiState.contactId)
    }


}

private fun LazyListScope.messages(uiState: ChatUiState) {
    when (uiState) {
        is ChatUiState.Loading -> {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize()
                )
            }
        }

        is ChatUiState.Success -> {
            item {
                ChatState(uiState = uiState)
            }
            uiState.messagesBySendTime.forEach { (sendTimeDay, messages) ->
                items(messages, key = { it.id ?: 0 }) { message ->
                    MessageItem(message = message)
                }
                item {
                    MessageDay(sendTime = sendTimeDay)
                }
            }
        }
    }
}

@Composable
fun MessageDay(
    sendTime: LocalDate, modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Text(
            text = sendTime.formatted,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun MessageItem(
    message: Message, modifier: Modifier = Modifier
) {
    val style = getMessageStyle(message.isMine)

    Box(
        contentAlignment = style.alignment, modifier = modifier.fillMaxWidth()
    ) {
        Surface(
            color = style.containerColor, shape = style.shape
        ) {
            Column(modifier = modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                MessageBody(message)
                MessageSubtitle(
                    message = message, modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun MessageBody(message: Message) {
    Text(text = message.body)
}

@Composable
private fun MessageSubtitle(
    message: Message, modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = message.sendTime.localTime,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.alpha(0.4f)
        )
        if (message.status == MessageStatus.SentDelivered) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Delivered",
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun ChatInput(
    uiState: ChatUiState, onUserTyping: (String) -> Unit, sendMessage: (String) -> Unit
) {
    val draftMessage = if (uiState is ChatUiState.Success) uiState.conversation.draftMessage ?: ""
    else ""

    val (messageText, setMessageText) = rememberSaveable(draftMessage) {
        mutableStateOf(draftMessage)
    }

    Surface(color = MaterialTheme.colorScheme.surface) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChatTextField(
                value = messageText,
                onValueChange = {
                    setMessageText(it)
                    onUserTyping(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .padding(start = 16.dp)
                    .weight(1f)
            )

            val isSendEnabled = messageText.isNotBlank()

            IconButton(
                onClick = {
                    sendMessage(messageText)
                    setMessageText("")
                }, enabled = isSendEnabled
            ) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Send",
                    tint = MaterialTheme.colorScheme.primary.copy(
                        alpha = if (isSendEnabled) 1f else 0.4f
                    )
                )
            }
        }
    }
}

@Composable
private fun getMessageStyle(isMine: Boolean): MessageStyle {
    return if (isMine) {
        MessageStyle(
            alignment = Alignment.CenterEnd,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)
        )
    } else {
        MessageStyle(
            alignment = Alignment.CenterStart,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            shape = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
        )
    }
}

@Composable
private fun ChatState(
    uiState: ChatUiState.Success
) {
    if (uiState.shouldShowChatState) {
        val postfixText = if (uiState.conversation.chatState == ChatState.Composing) "is typing..."
        else "stopped typing."
        Text(
            text = "${uiState.conversation.peerLocalPart} $postfixText",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

private data class MessageStyle(
    val alignment: Alignment, val containerColor: Color, val shape: Shape
)