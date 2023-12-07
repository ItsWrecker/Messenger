package com.qxlabai.messenger.service.xmpp.notifications

import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smackx.push_notifications.PushNotificationsManager

interface XmppPushNotification {


    fun initialize(xmppConnection: XMPPTCPConnection)
}