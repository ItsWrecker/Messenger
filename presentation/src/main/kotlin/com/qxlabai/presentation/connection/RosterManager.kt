package com.qxlabai.presentation.connection

import org.jivesoftware.smack.tcp.XMPPTCPConnection

interface RosterManager {

    suspend fun initialize(connection: XMPPTCPConnection)

    fun onCleared()
}