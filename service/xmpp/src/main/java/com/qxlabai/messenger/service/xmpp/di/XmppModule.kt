package com.qxlabai.messenger.service.xmpp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.qxlabai.messenger.service.xmpp.MessageManager
import com.qxlabai.messenger.service.xmpp.MessageManagerImpl
import com.qxlabai.messenger.service.xmpp.RosterManager
import com.qxlabai.messenger.service.xmpp.RosterManagerImpl

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
}
