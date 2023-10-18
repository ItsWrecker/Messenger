package com.qxlabai.presentation.connection





import com.qxlabai.domain.entity.ChatState
import org.jivesoftware.smackx.chatstates.ChatState as SmackChatState
import org.jivesoftware.smackx.chatstates.ChatState.active
import org.jivesoftware.smackx.chatstates.ChatState.composing
import org.jivesoftware.smackx.chatstates.ChatState.gone
import org.jivesoftware.smackx.chatstates.ChatState.inactive
import org.jivesoftware.smackx.chatstates.ChatState.paused

fun SmackChatState.asExternalEnum() = when (this) {
    active -> ChatState.Active
    inactive -> ChatState.Inactive
    composing -> ChatState.Composing
    paused -> ChatState.Paused
    gone -> ChatState.Gone
}

fun ChatState.asSmackEnum() = when (this) {
    ChatState.Active -> active
    ChatState.Inactive, ChatState.Idle -> inactive
    ChatState.Composing -> composing
    ChatState.Paused -> paused
    ChatState.Gone -> gone
}
