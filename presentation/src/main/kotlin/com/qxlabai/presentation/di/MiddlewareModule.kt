package com.qxlabai.presentation.di

import com.qxlabai.presentation.core.Middleware
import com.qxlabai.presentation.lock.LockAction
import com.qxlabai.presentation.lock.LockMiddleware
import com.qxlabai.presentation.lock.LockState
import com.qxlabai.presentation.xmpp.XmppAction
import com.qxlabai.presentation.xmpp.XmppMiddleware
import com.qxlabai.presentation.xmpp.XmppState
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
}