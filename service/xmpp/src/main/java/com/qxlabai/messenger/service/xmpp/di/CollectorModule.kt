package com.qxlabai.messenger.service.xmpp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.qxlabai.messenger.service.xmpp.collector.AccountsCollector
import com.qxlabai.messenger.service.xmpp.collector.AccountsCollectorImpl
import com.qxlabai.messenger.service.xmpp.collector.ChatStateCollector
import com.qxlabai.messenger.service.xmpp.collector.ChatStateCollectorImpl
import com.qxlabai.messenger.service.xmpp.collector.ContactsCollector
import com.qxlabai.messenger.service.xmpp.collector.ContactsCollectorImpl
import com.qxlabai.messenger.service.xmpp.collector.MessagesCollector
import com.qxlabai.messenger.service.xmpp.collector.MessagesCollectorImpl

@Module
@InstallIn(SingletonComponent::class)
interface CollectorModule {

    @Binds
    fun bindsAccountsCollector(
        accountsCollector: AccountsCollectorImpl
    ): AccountsCollector

    @Binds
    fun bindsContactsCollector(
        contactsCollector: ContactsCollectorImpl
    ): ContactsCollector

    @Binds
    fun bindsMessagesCollector(
        messagesCollector: MessagesCollectorImpl
    ): MessagesCollector

    @Binds
    fun bindsChatStateCollector(
        chatStateCollector: ChatStateCollectorImpl
    ): ChatStateCollector
}
