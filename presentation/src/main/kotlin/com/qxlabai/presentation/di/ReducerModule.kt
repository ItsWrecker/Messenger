package com.qxlabai.presentation.di

import com.qxlabai.presentation.core.Reducer
import com.qxlabai.presentation.lock.LockAction
import com.qxlabai.presentation.lock.LockReducer
import com.qxlabai.presentation.lock.LockState
import com.qxlabai.presentation.xmpp.XmppAction
import com.qxlabai.presentation.xmpp.XmppReducer
import com.qxlabai.presentation.xmpp.XmppState
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ReducerModule {

    @Binds
    @Singleton
    fun bindXmppReducer(
        impl: XmppReducer
    ): Reducer<XmppState, XmppAction>


    @Binds
    @Singleton
    fun bindLockReducer(
        impl: LockReducer
    ): Reducer<LockState, LockAction>
}