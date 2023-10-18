package com.qxlabai.data.di

import com.qxlabai.data.xmpp.collectors.AccountsCollectorImpl
import com.qxlabai.data.xmpp.collectors.ChatStateCollectorImpl
import com.qxlabai.data.xmpp.collectors.ContactsCollectorImpl
import com.qxlabai.data.xmpp.collectors.MessagesCollectorImpl
import com.qxlabai.domain.collectors.AccountsCollector
import com.qxlabai.domain.collectors.ChatStateCollector
import com.qxlabai.domain.collectors.ContactsCollector
import com.qxlabai.domain.collectors.MessagesCollector
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CollectorModule {

    @Binds
    fun bindsAccountsCollector(
        impl: AccountsCollectorImpl
    ): AccountsCollector

    @Binds
    fun bindsChatsCollector(
        impl: ChatStateCollectorImpl
    ): ChatStateCollector

    @Binds
    fun bindContactsCollector(
        impl: ContactsCollectorImpl
    ): ContactsCollector

    @Binds
    fun bindsMessageCollector(
        impl: MessagesCollectorImpl
    ): MessagesCollector
}