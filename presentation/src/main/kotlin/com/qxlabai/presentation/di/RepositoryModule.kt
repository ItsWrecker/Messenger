package com.qxlabai.presentation.di

import com.qxlabai.data.datastore.repository.AppLockRepositoryImpl
import com.qxlabai.data.datastore.repository.CredentialsRepositoryImpl
import com.qxlabai.data.xmpp.XmppRepositoryImpl
import com.qxlabai.domain.repositories.AppLockRepository
import com.qxlabai.domain.repositories.CredentialsRepository
import com.qxlabai.domain.repositories.XmppRepository
import com.qxlabai.presentation.notification.NotificationRepository
import com.qxlabai.presentation.notification.NotificationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindsLockRepository(
        impl: AppLockRepositoryImpl
    ): AppLockRepository

    @Binds
    @Singleton
    fun bindXmppRepository(
        impl: XmppRepositoryImpl
    ): XmppRepository


    @Binds
    @Singleton
    fun bindsCredentialRepository(
        impl: CredentialsRepositoryImpl
    ): CredentialsRepository

    @Binds
    @Singleton
    fun bindsNotificationManager(
        impl: NotificationRepositoryImpl
    ): NotificationRepository

}