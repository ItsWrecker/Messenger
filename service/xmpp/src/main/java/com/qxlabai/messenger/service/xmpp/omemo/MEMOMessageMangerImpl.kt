package com.qxlabai.messenger.service.xmpp.omemo

import org.jivesoftware.smack.tcp.XMPPTCPConnection

interface MEMOMessageManger {

    suspend fun initialize(connection: XMPPTCPConnection)

    fun onCleared()
}