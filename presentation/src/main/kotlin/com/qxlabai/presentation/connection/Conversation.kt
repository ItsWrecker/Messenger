package com.qxlabai.presentation.connection

import com.qxlabai.domain.entity.Conversation
import org.jxmpp.jid.EntityBareJid


fun EntityBareJid.asConversation() = Conversation(
    peerJid = asBareJid().toString(),
)