package com.qxlabai.presentation.xmpp.auth

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qxlabai.presentation.core.Store
import com.qxlabai.presentation.xmpp.services.XmppService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val store: Store<AuthState, AuthAction>,
) : ViewModel() {

    @SuppressLint("StaticFieldLeak")
    private lateinit var xmppService: XmppService
    private lateinit var binder: XmppService.XmppBinder
    private var isConnected: Boolean = false

    val viewState: StateFlow<AuthState> = store.state


    fun updateUUID(uuid: String) = viewModelScope.launch {
        store.dispatch(AuthAction.UpdateUUID(uuid))
    }

    fun updatePasscode(passcode: String) = viewModelScope.launch {
        store.dispatch(AuthAction.UpdatePasscode(passcode))
    }

    fun authenticate(context: Context) = viewModelScope.launch {
        context.startService(Intent(context, XmppService::class.java).also {
            it.action = XmppService.COMMAND_AUTH
        })
        store.dispatch(AuthAction.Authenticate)
    }


}