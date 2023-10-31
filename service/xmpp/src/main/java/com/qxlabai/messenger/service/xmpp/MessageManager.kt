package com.qxlabai.messenger.service.xmpp

import org.jivesoftware.smack.packet.Stanza
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smackx.omemo.OmemoManager
import org.jivesoftware.smackx.omemo.OmemoMessage

interface MessageManager {

    suspend fun initialize(connection: XMPPTCPConnection, omemoManager: OmemoManager)

    fun onCleared()

    fun handleIncomingMessage(stanza: Stanza?, decryptedMessage: OmemoMessage.Received?)
}
