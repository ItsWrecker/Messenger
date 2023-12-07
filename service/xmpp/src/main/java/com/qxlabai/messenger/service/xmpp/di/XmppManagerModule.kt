package com.qxlabai.messenger.service.xmpp.di

import android.content.Context
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
import com.qxlabai.messenger.service.xmpp.notification.NotificationManager
import com.qxlabai.messenger.service.xmpp.notifications.XmppPushNotification
import dagger.hilt.android.qualifiers.ApplicationContext
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
        @Dispatcher(IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationContext context: Context,
        xmppPushNotification: XmppPushNotification,
        notificationManager: NotificationManager
    ): XmppManager {
        return XmppManagerImpl(
            rosterManager = rosterManager,
            messageManager = messageManager,
            preferencesRepository = preferencesRepository,
            ioDispatcher = ioDispatcher,
            context = context,
            xmppPushNotification = xmppPushNotification,
            notificationManager = notificationManager
        )
    }
}
