package com.qxlabai.messenger.service.xmpp.model

import com.qxlabai.messenger.core.model.data.Message
import org.jivesoftware.smack.packet.Message as SmackMessage

fun SmackMessage.asExternalModel() = Message.createReceivedMessage(
    stanzaId = stanzaId,
    peerJid = from.asBareJid().toString(),
    text = body
)


