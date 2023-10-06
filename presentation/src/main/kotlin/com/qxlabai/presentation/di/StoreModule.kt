package com.qxlabai.presentation.di

import com.qxlabai.presentation.core.Middleware
import com.qxlabai.presentation.core.Reducer
import com.qxlabai.presentation.core.Store
import com.qxlabai.presentation.lock.LockAction
import com.qxlabai.presentation.lock.LockState
import com.qxlabai.presentation.xmpp.auth.AuthAction
import com.qxlabai.presentation.xmpp.auth.AuthState
import com.qxlabai.presentation.xmpp.connection.XmppAction
import com.qxlabai.presentation.xmpp.connection.XmppState
import com.qxlabai.presentation.xmpp.profile.ProfileAction
import com.qxlabai.presentation.xmpp.profile.ProfileState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object StoreModule {

    @Provides
    @Singleton
    fun providesXmppStore(
        middleware: Middleware<XmppState, XmppAction>,
        reducer: Reducer<XmppState, XmppAction>
    ): Store<XmppState, XmppAction> = Store(
        initialState = XmppState(isConnectionEstablished = false),
        reducer = reducer,
        middleware = middleware
    )

    @Provides
    @Singleton
    fun provideLockStore(
        middleware: Middleware<LockState, LockAction>,
        reducer: Reducer<LockState, LockAction>
    ): Store<LockState, LockAction> = Store(
        initialState = LockState(isVerified = false),
        reducer = reducer,
        middleware = middleware
    )

    @Provides
    @Singleton
    fun provideAuthStore(
        middleware: Middleware<AuthState, AuthAction>,
        reducer: Reducer<AuthState, AuthAction>
    ): Store<AuthState, AuthAction> = Store(
        initialState = AuthState(),
        reducer = reducer,
        middleware = middleware
    )
    @Provides
    @Singleton
    fun provideProfileStore(
        middleware: Middleware<ProfileState, ProfileAction>,
        reducer: Reducer<ProfileState, ProfileAction>
    ): Store<ProfileState, ProfileAction> = Store(
        initialState = ProfileState(),
        reducer = reducer,
        middleware = middleware
    )
}