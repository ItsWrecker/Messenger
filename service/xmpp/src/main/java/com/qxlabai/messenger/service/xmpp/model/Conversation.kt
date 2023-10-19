package com.qxlabai.messenger.service.xmpp.model

import com.qxlabai.messenger.core.model.data.Conversation
import org.jxmpp.jid.EntityBareJid

fun EntityBareJid.asConversation() = Conversation(
    peerJid = asBareJid().toString(),
)
