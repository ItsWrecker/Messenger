package com.qxlabai.presentation.di

import com.qxlabai.presentation.core.Reducer
import com.qxlabai.presentation.lock.LockAction
import com.qxlabai.presentation.lock.LockReducer
import com.qxlabai.presentation.lock.LockState
import com.qxlabai.presentation.xmpp.auth.AuthAction
import com.qxlabai.presentation.xmpp.auth.AuthReducer
import com.qxlabai.presentation.xmpp.auth.AuthState
import com.qxlabai.presentation.xmpp.connection.XmppAction
import com.qxlabai.presentation.xmpp.connection.XmppReducer
import com.qxlabai.presentation.xmpp.connection.XmppState
import com.qxlabai.presentation.xmpp.profile.ProfileAction
import com.qxlabai.presentation.xmpp.profile.ProfileReducer
import com.qxlabai.presentation.xmpp.profile.ProfileState
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

    @Binds
    @Singleton
    fun bindAuthReducer(
        impl: AuthReducer
    ): Reducer<AuthState, AuthAction>

    @Binds
    @Singleton
    fun bindsProfileReducer(
        impl: ProfileReducer
    ): Reducer<ProfileState, ProfileAction>
}