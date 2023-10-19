package com.qxlabai.messenger.core.data.model

import com.qxlabai.messenger.core.model.data.Message
import com.qxlabai.messenger.core.database.model.MessageEntity

fun Message.asEntity() = MessageEntity(
    id = id ?: 0,
    stanzaId = stanzaId,
    peerJid = peerJid,
    body = body,
    sendTime = sendTime,
    status = status
)
