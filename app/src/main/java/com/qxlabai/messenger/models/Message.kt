package com.qxlabai.messenger.models

import java.util.UUID

data class Message(
    val message: String,
    val userName: String,
    val timestamp: Long,
    val id: String = UUID.randomUUID().toString()
)