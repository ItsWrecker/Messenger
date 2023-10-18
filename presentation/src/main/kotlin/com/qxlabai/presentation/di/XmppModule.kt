package com.qxlabai.presentation.di

import com.qxlabai.presentation.connection.XmppManager
import com.qxlabai.presentation.connection.XmppManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface XmppModule {

    @Binds
    fun bindXmppManager(
        impl: XmppManagerImpl
    ): XmppManager
}