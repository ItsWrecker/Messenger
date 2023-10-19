package com.qxlabai.messenger.core.database.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.qxlabai.messenger.core.database.transaction.MessageTransaction
import com.qxlabai.messenger.core.database.transaction.MessageTransactionImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TransactionsModule {

    @Singleton
    @Binds
    fun bindsMessageTransaction(
        messageTransaction: MessageTransactionImpl
    ): MessageTransaction
}
