package com.qxlabai.presentation.di

import com.qxlabai.presentation.core.Middleware
import com.qxlabai.presentation.core.Reducer
import com.qxlabai.presentation.lock.LockAction
import com.qxlabai.presentation.lock.LockMiddleware
import com.qxlabai.presentation.lock.LockState
import com.qxlabai.presentation.xmpp.auth.AuthAction
import com.qxlabai.presentation.xmpp.auth.AuthMiddleware
import com.qxlabai.presentation.xmpp.auth.AuthState
import com.qxlabai.presentation.xmpp.connection.XmppAction
import com.qxlabai.presentation.xmpp.connection.XmppMiddleware
import com.qxlabai.presentation.xmpp.connection.XmppState
import com.qxlabai.presentation.xmpp.profile.ProfileAction
import com.qxlabai.presentation.xmpp.profile.ProfileMiddleware
import com.qxlabai.presentation.xmpp.profile.ProfileReducer
import com.qxlabai.presentation.xmpp.profile.ProfileState
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface MiddlewareModule {

    @Binds
    @Singleton
    fun bindXmppMiddleware(
        impl: XmppMiddleware
    ): Middleware<XmppState, XmppAction>

    @Binds
    @Singleton
    fun bindLockMiddleware(
        impl: LockMiddleware
    ): Middleware<LockState, LockAction>

    @Binds
    @Singleton
    fun bindAuthViewModel(
        impl: AuthMiddleware
    ): Middleware<AuthState, AuthAction>


    @Binds
    @Singleton
    fun bindsProfileMiddleware(
        impl: ProfileMiddleware
    ): Middleware<ProfileState, ProfileAction>


}