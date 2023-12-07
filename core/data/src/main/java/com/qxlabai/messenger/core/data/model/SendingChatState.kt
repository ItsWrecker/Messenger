package com.qxlabai.messenger.core.data.model

import com.qxlabai.messenger.core.model.data.SendingChatState
import com.qxlabai.messenger.core.database.model.SendingChatStateEntity

fun SendingChatState.asEntity() = SendingChatStateEntity(
    id = id ?: 0,
    peerJid = peerJid,
    chatState = chatState,
    consumed = consumed
)
