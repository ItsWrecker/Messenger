package com.qxlabai.model

import com.qxlabai.model.ChatState

data class SendingChatState(
    val id: Long? = null,
    val peerJid: String,
    val chatState: ChatState,
    val consumed: Boolean = false
)
