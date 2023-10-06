package com.qxlabai.presentation.xmpp.connection

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qxlabai.presentation.core.Store
import com.qxlabai.presentation.xmpp.services.CredentialsParcel
import com.qxlabai.presentation.xmpp.services.XmppService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.XMPPConnection
import java.lang.Exception
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
    private lateinit var binder: XmppService.XmppBinder

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            binder = service as XmppService.XmppBinder
            xmppService = binder.getService()
            isBound = true
            viewModelScope.launch {
                store.dispatch(XmppAction.ServiceReady)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }


    fun bindService(context: Context) = viewModelScope.launch {
        store.dispatch(XmppAction.ServiceStart)
        Intent(context, XmppService::class.java).also {
            context.bindService(it, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }


    fun authenticate(context: Context, credentialsParcel: CredentialsParcel) {
        context.startService(Intent(context, XmppService::class.java).also {
            it.action = XmppService.COMMAND_AUTH
            it.putExtra(XmppService.DATA_CREDENTIALS, Bundle().apply {
                this.putParcelable(XmppService.DATA_CREDENTIALS, credentialsParcel)
            })
        })
        viewModelScope.launch {
            store.dispatch(
                XmppAction.Authenticate(
                    credentialsParcel.uuid,
                    credentialsParcel.passcode
                )
            )
        }
    }
    fun processAction(action: XmppAction) = viewModelScope.launch {
        return@launch store.dispatch(action)
    }

}