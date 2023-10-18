package com.qxlabai.data.di

import android.content.Context
import androidx.room.Room
import com.qxlabai.data.databse.MessengerDatabase
import com.qxlabai.data.databse.dao.ContactDao
import com.qxlabai.data.databse.dao.ConversationDao
import com.qxlabai.data.databse.dao.MessageDao
import com.qxlabai.data.databse.dao.SendingChatStateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDialogDatabase(
        @ApplicationContext context: Context,
    ): MessengerDatabase = Room.databaseBuilder(
        context,
        MessengerDatabase::class.java,
        "database.db"
    ).build()



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