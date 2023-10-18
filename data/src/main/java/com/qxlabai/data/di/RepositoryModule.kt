package com.qxlabai.data.di

import com.qxlabai.data.datastore.repository.ContactsRepositoryImpl
import com.qxlabai.data.datastore.repository.ConversationRepositoryImpl
import com.qxlabai.data.datastore.repository.MessageRepositoryImpl
import com.qxlabai.data.datastore.repository.PreferencesRepositoryImpl
import com.qxlabai.data.datastore.repository.SendingChatStatesRepositoryImpl
import com.qxlabai.domain.collectors.ContactsRepository
import com.qxlabai.domain.collectors.MessagesRepository
import com.qxlabai.domain.collectors.SendingChatStatesRepository
import com.qxlabai.domain.repositories.ConversationsRepository
import com.qxlabai.domain.repositories.PreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsPreferenceRepository(
        impl: PreferencesRepositoryImpl
    ): PreferencesRepository

    @Binds
    fun bindConversationsRepository(
        impl: ConversationRepositoryImpl
    ): ConversationsRepository

    @Binds
    fun bindsContactsRepository(
        impl: ContactsRepositoryImpl
    ): ContactsRepository

    @Binds
    fun bindMessageRepository(
        impl: MessageRepositoryImpl
    ): MessagesRepository


    @Binds
    fun bindsSendingChatsRepository(
        impl: SendingChatStatesRepositoryImpl
    ): SendingChatStatesRepository

}