package com.qxlabai.messenger.core.common.coroutines.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.qxlabai.messenger.core.common.coroutines.MessengerDispatchers.IO
import com.qxlabai.messenger.core.common.coroutines.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    @Dispatcher(IO)
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}
