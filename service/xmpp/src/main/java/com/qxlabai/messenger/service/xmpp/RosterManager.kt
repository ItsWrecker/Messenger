package com.qxlabai.messenger.service.xmpp

import org.jivesoftware.smack.tcp.XMPPTCPConnection

interface RosterManager {

    suspend fun initialize(connection: XMPPTCPConnection)

    fun onCleared()

    fun clearContacts(connection: XMPPTCPConnection)
}
