package com.qxlabai.messenger.service.xmpp.di

import com.qxlabai.messenger.service.xmpp.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val REQUEST_TIMEOUT: Long = 10L
    private const val TAG = "HTTP"

    private val interceptor = HttpLoggingInterceptor().also {
        it.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun providesRetrofitApi(): Api = Retrofit.Builder()
        .baseUrl("https://fcm.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor( interceptor)
            .build())
        .build()
        .create(Api::class.java)



}