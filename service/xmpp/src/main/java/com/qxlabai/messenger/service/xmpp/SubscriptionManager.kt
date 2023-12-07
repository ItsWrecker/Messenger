package com.qxlabai.messenger.service.xmpp

import org.jivesoftware.smack.roster.SubscribeListener
import org.jivesoftware.smack.tcp.XMPPTCPConnection

interface SubscriptionManager {

    suspend fun getSubscribeListener(connection: XMPPTCPConnection): SubscribeListener

}