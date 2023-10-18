package com.qxlabai.data.di

import com.qxlabai.data.databse.transaction.MessageTransaction
import com.qxlabai.data.databse.transaction.MessageTransactionImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface TransactionsModule {

    @Binds
    fun bindTransaction(
        impl: MessageTransactionImpl
    ): MessageTransaction
}