package com.qxlabai.presentation.di

import com.qxlabai.presentation.core.Middleware
import com.qxlabai.presentation.core.Reducer
import com.qxlabai.presentation.core.Store
import com.qxlabai.presentation.lock.LockAction
import com.qxlabai.presentation.lock.LockMiddleware
import com.qxlabai.presentation.lock.LockState
import com.qxlabai.presentation.xmpp.XmppAction
import com.qxlabai.presentation.xmpp.XmppMiddleware
import com.qxlabai.presentation.xmpp.XmppReducer
import com.qxlabai.presentation.xmpp.XmppState
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
}