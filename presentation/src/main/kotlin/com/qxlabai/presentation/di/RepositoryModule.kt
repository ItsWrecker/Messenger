package com.qxlabai.presentation.di

import com.qxlabai.data.datastore.repository.AppLockRepositoryImpl
import com.qxlabai.domain.repositories.AppLockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsLockRepository(
        impl: AppLockRepositoryImpl
    ): AppLockRepository


}