package com.qxlabai.presentation.di

import com.qxlabai.presentation.connection.MessageManager
import com.qxlabai.presentation.connection.MessageManagerImpl
import com.qxlabai.presentation.connection.RosterManager
import com.qxlabai.presentation.connection.RosterManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ConnectionModule {

    @Binds
    fun bindMessageManager(
        impl: MessageManagerImpl
    ): MessageManager



    @Binds
    fun bindsRosterManager(
        impl: RosterManagerImpl
    ): RosterManager

}