package com.qxlabai.messenger.service.xmpp.model

import com.qxlabai.messenger.core.model.data.ChatState
import com.qxlabai.messenger.core.model.data.ChatState.Active
import com.qxlabai.messenger.core.model.data.ChatState.Composing
import com.qxlabai.messenger.core.model.data.ChatState.Gone
import com.qxlabai.messenger.core.model.data.ChatState.Idle
import com.qxlabai.messenger.core.model.data.ChatState.Inactive
import com.qxlabai.messenger.core.model.data.ChatState.Paused
import org.jivesoftware.smackx.chatstates.ChatState as SmackChatState
import org.jivesoftware.smackx.chatstates.ChatState.active
import org.jivesoftware.smackx.chatstates.ChatState.composing
import org.jivesoftware.smackx.chatstates.ChatState.gone
import org.jivesoftware.smackx.chatstates.ChatState.inactive
import org.jivesoftware.smackx.chatstates.ChatState.paused


fun SmackChatState.asExternalEnum() = when (this) {
    active -> Active
    inactive -> Inactive
    composing -> Composing
    paused -> Paused
    gone -> Gone
}

fun ChatState.asSmackEnum() = when (this) {
    Active -> active
    Inactive, Idle -> inactive
    Composing -> composing
    Paused -> paused
    Gone -> gone
}
