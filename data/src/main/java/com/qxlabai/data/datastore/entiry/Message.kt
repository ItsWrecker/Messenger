package com.qxlabai.data.datastore.entiry

import com.qxlabai.data.databse.entity.MessageEntity
import com.qxlabai.domain.entity.Message


fun Message.asEntity() = MessageEntity(
    id = id ?: 0,
    stanzaId = stanzaId,
    peerJid = peerJid,
    body = body,
    sendTime = sendTime,
    status = status
)
