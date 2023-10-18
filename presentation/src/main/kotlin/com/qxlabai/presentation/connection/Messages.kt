package com.qxlabai.presentation.connection



import com.qxlabai.domain.entity.Message
import org.jivesoftware.smack.packet.Message as SmackMessage
fun SmackMessage.asExternalModel() = Message.createReceivedMessage(
    stanzaId = stanzaId,
    peerJid = from.asBareJid().toString(),
    text = body
)
