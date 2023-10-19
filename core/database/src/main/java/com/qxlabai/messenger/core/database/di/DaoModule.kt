package com.qxlabai.messenger.core.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.qxlabai.messenger.core.database.MessengerDatabase
import com.qxlabai.messenger.core.database.dao.ContactDao
import com.qxlabai.messenger.core.database.dao.ConversationDao
import com.qxlabai.messenger.core.database.dao.MessageDao
import com.qxlabai.messenger.core.database.dao.SendingChatStateDao

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun providesContactDao(
        database: MessengerDatabase,
    ): ContactDao = database.contactDao()

    @Provides
    fun providesMessageDao(
        database: MessengerDatabase,
    ): MessageDao = database.messageDao()

    @Provides
    fun providesConversationDao(
        database: MessengerDatabase,
    ): ConversationDao = database.conversationDao()

    @Provides
    fun providesSendingChatStateDao(
        database: MessengerDatabase,
    ): SendingChatStateDao = database.sendingChatStateDao()
}
