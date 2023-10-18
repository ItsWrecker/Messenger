package com.qxlabai.presentation.di

import com.qxlabai.domain.core.Dispatcher
import com.qxlabai.domain.core.MessengerDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    fun provideIODispatcher(
    ): CoroutineDispatcher = Dispatchers.IO
}