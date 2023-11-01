package com.qxlabai.messenger.service.xmpp

import com.qxlabai.messenger.core.model.data.Account
import org.jivesoftware.smack.tcp.XMPPTCPConnection

interface XmppManager {

    suspend fun initialize()

    fun purseDevice()

    fun getConnection(): XMPPTCPConnection

    suspend fun login(account: Account)

    suspend fun register(account: Account)

    fun onCleared()
}
