package com.qxlabai.messenger.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.qxlabai.messenger.core.data.repository.ContactsRepository
import com.qxlabai.messenger.core.data.repository.ConversationsRepository
import com.qxlabai.messenger.core.data.repository.MessagesRepository
import com.qxlabai.messenger.core.data.repository.ContactsRepositoryImpl
import com.qxlabai.messenger.core.data.repository.ConversationsRepositoryImpl
import com.qxlabai.messenger.core.data.repository.EraseRepository
import com.qxlabai.messenger.core.data.repository.EraseRepositoryImpl
import com.qxlabai.messenger.core.data.repository.LockRepository
import com.qxlabai.messenger.core.data.repository.LockRepositoryImpl
import com.qxlabai.messenger.core.data.repository.MessagesRepositoryImpl
import com.qxlabai.messenger.core.data.repository.PreferencesRepositoryImpl
import com.qxlabai.messenger.core.data.repository.SendingChatStatesRepositoryImpl
import com.qxlabai.messenger.core.data.repository.PreferencesRepository
import com.qxlabai.messenger.core.data.repository.SendingChatStatesRepository

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsPreferencesRepository(
        preferencesRepository: PreferencesRepositoryImpl
    ): PreferencesRepository

    @Binds
    fun bindsContactsRepository(
        contactsRepository: ContactsRepositoryImpl
    ): ContactsRepository

    @Binds
    fun bindsMessagesRepository(
        messagesRepository: MessagesRepositoryImpl
    ): MessagesRepository

    @Binds
    fun bindsConversationsRepository(
        conversationsRepository: ConversationsRepositoryImpl
    ): ConversationsRepository

    @Binds
    fun bindsSendingChatStatesRepository(
        sendingChatStatesRepository: SendingChatStatesRepositoryImpl
    ): SendingChatStatesRepository

    @Binds
    fun bindsLockRepository(
        impl: LockRepositoryImpl
    ): LockRepository

    @Binds
    fun bindEraseRepository(
        impl: EraseRepositoryImpl
    ): EraseRepository

}
