package com.qxlabai.presentation.connection

import com.qxlabai.domain.entity.Account
import org.jivesoftware.smack.tcp.XMPPTCPConnection

interface XmppManager {

    suspend fun initialize()

    fun getConnection(): XMPPTCPConnection

    suspend fun login(account: Account)

    suspend fun register(account: Account)

    fun onCleared()
}