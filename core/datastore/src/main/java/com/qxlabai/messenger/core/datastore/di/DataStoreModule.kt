package com.qxlabai.messenger.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.qxlabai.messenger.core.common.coroutines.MessengerDispatchers.IO
import com.qxlabai.messenger.core.common.coroutines.Dispatcher
import com.qxlabai.messenger.core.datastore.UserPreferences
import com.qxlabai.messenger.core.datastore.UserPreferencesSerializer
import com.qxlabai.messenger.core.datastore.erase.ErasePreferences
import com.qxlabai.messenger.core.datastore.erase.EraseSerializer
import com.qxlabai.messenger.core.datastore.lock.LockPreferenceSerializer
import com.qxlabai.messenger.core.datastore.lock.LockPreferences
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providesUserPreferencesDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(IO) ioDispatcher: CoroutineDispatcher,
        userPreferencesSerializer: UserPreferencesSerializer
    ): DataStore<UserPreferences> =
        DataStoreFactory.create(
            serializer = userPreferencesSerializer,
            scope = CoroutineScope(ioDispatcher + SupervisorJob()),
        ) {
            context.dataStoreFile("user_preferences.pb")
        }

    @Provides
    @Singleton
    fun providesLockPreferences(
        @ApplicationContext context: Context,
        @Dispatcher(IO) ioDispatcher: CoroutineDispatcher,
        lockPreferenceSerializer: LockPreferenceSerializer
    ): DataStore<LockPreferences> = DataStoreFactory.create(
        serializer = lockPreferenceSerializer,
        scope = CoroutineScope(ioDispatcher + SupervisorJob())
    ) {
        context.dataStoreFile("lock_preferences.pb")
    }

    @Provides
    @Singleton
    fun provideErasePreferences(
        @ApplicationContext context: Context,
        @Dispatcher(IO) ioDispatcher: CoroutineDispatcher,
        erasePreferences: EraseSerializer
    ): DataStore<ErasePreferences> = DataStoreFactory.create(
        serializer = erasePreferences,
        scope = CoroutineScope(ioDispatcher + SupervisorJob())
    ) {
        context.dataStoreFile("incorrect_passcode.pb")
    }


}
