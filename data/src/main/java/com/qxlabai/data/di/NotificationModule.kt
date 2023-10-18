package com.qxlabai.data.di

import android.app.Notification
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {


    fun provideNotification(

    ): Notification {
        return Notification()
    }


}