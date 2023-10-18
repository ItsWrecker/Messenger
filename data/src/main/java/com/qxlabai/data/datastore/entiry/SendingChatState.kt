package com.qxlabai.data.datastore.entiry

import com.qxlabai.data.databse.entity.SendingChatStateEntity
import com.qxlabai.domain.entity.SendingChatState
fun SendingChatState.asEntity() = SendingChatStateEntity(
    id = id ?: 0,
    peerJid = peerJid,
    chatState = chatState,
    consumed = consumed
)
