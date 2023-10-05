package com.qxlabai.presentation.xmpp

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qxlabai.presentation.core.Store
import com.qxlabai.presentation.xmpp.services.XmppService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class XmppViewModel @Inject constructor(
    private val store: Store<XmppState, XmppAction>
) : ViewModel() {


    val viewState: StateFlow<XmppState> = store.state


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
        return@launch store.dispatch(action)
    }

}