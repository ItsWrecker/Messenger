package com.qxlabai.messenger.service.xmpp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.qxlabai.messenger.service.xmpp.MessageManager
import com.qxlabai.messenger.service.xmpp.MessageManagerImpl
import com.qxlabai.messenger.service.xmpp.RosterManager
import com.qxlabai.messenger.service.xmpp.RosterManagerImpl
import com.qxlabai.messenger.service.xmpp.SubscriptionManager
import com.qxlabai.messenger.service.xmpp.SubscriptionManagerImpl
import com.qxlabai.messenger.service.xmpp.notifications.XmppPushNotification
import com.qxlabai.messenger.service.xmpp.notifications.XmppPushNotificationImpl

@Module
@InstallIn(SingletonComponent::class)
interface XmppModule {

    @Binds
    fun bindsRosterManager(
        rosterManager: RosterManagerImpl
    ): RosterManager

    @Binds
    fun bindsMessageManager(
        messageManagerImpl: MessageManagerImpl
    ): MessageManager

    @Binds
    fun bindsSubscriptionManager(
        impl: SubscriptionManagerImpl
    ): SubscriptionManager

    @Binds
    fun bindXmppPushNotification(
        impl: XmppPushNotificationImpl
    ): XmppPushNotification
}
