package com.qxlabai.messenger.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.qxlabai.messenger.core.database.MessengerDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesDialogDatabase(
        @ApplicationContext context: Context,
    ): MessengerDatabase = Room.databaseBuilder(
        context,
        MessengerDatabase::class.java,
        "dialogue-database"
    ).build()
}
