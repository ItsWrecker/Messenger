package com.qxlabai.messenger.service.xmpp

import org.jivesoftware.smack.roster.SubscribeListener

interface SubscriptionManager {

    suspend fun getSubscribeListener(): SubscribeListener

}