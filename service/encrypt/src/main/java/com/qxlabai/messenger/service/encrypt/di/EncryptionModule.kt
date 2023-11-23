package com.qxlabai.messenger.service.encrypt.di
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface EncryptionModule {


//    @Binds
//    @Singleton
//    fun bindsEncryptionManager(
//        impl: CryptoManagerImpl
//    ): CryptoManager
}