package com.qxlabai.messenger.service.xmpp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.qxlabai.messenger.core.common.coroutines.MessengerDispatchers.IO
import com.qxlabai.messenger.core.common.coroutines.Dispatcher
import com.qxlabai.messenger.core.data.repository.PreferencesRepository
import com.qxlabai.messenger.service.xmpp.MessageManager
import com.qxlabai.messenger.service.xmpp.RosterManager
import com.qxlabai.messenger.service.xmpp.XmppManager
import com.qxlabai.messenger.service.xmpp.XmppManagerImpl
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
class XmppManagerModule {

    @Provides
    @Singleton
    fun providesXmppManager(
        rosterManager: RosterManager,
        messageManager: MessageManager,
        preferencesRepository: PreferencesRepository,
        @Dispatcher(IO) ioDispatcher: CoroutineDispatcher
    ): XmppManager {
        return XmppManagerImpl(
            rosterManager = rosterManager,
            messageManager = messageManager,
            preferencesRepository = preferencesRepository,
            ioDispatcher = ioDispatcher
        )
    }
}
