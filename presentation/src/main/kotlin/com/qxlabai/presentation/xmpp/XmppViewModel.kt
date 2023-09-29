package com.qxlabai.presentation.xmpp

import android.annotation.SuppressLint
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qxlabai.domain.events.Events
import com.qxlabai.domain.interactors.xmpp.AuthenticateUseCase
import com.qxlabai.domain.interactors.xmpp.ConnectXmppUseCase
import com.qxlabai.domain.interactors.xmpp.entity.Credentials
import com.qxlabai.presentation.core.Store
import com.qxlabai.presentation.xmpp.services.XmppService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class XmppViewModel @Inject constructor(
    private val store: Store<XmppState, XmppAction>,
    private val connectXmppUseCase: ConnectXmppUseCase,
    private val authenticateUseCase: AuthenticateUseCase
) : ViewModel() {

    private val TAG = this::class.java.simpleName

    private var isBound: Boolean = false
    private lateinit var xmppService: XmppService

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as XmppService.XmppBinder
            xmppService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    fun bindService(context: Context) = viewModelScope.launch {
        Intent(context, XmppService::class.java).also {
            context.bindService(it, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    fun processAction(action: XmppAction) = viewModelScope.launch {
        when (action) {
            XmppAction.Connect -> connectXmppUseCase.invoke(Unit).collectLatest { events ->
                when (events) {
                    is Events.Error -> Log.e(TAG, events.message, events.cause)
                    is Events.Loading -> Log.e(TAG, events.message)
                    is Events.Success -> {
                        Log.e(TAG, events.data.toString())

                        if (events.data) {
                            authenticateUseCase.invoke(
                                Credentials(
                                    "santos",
                                    "Wrecker6580@"
                                )
                            ).collectLatest {
                                Log.e(TAG, it.toString())
                            }
                        }
                    }
                }
            }
        }
    }

}