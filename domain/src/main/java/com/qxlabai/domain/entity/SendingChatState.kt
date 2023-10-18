package com.qxlabai.domain.entity

data class SendingChatState(
    val id: Long? = null,
    val peerJid: String,
    val chatState: ChatState,
    val consumed: Boolean = false
)
