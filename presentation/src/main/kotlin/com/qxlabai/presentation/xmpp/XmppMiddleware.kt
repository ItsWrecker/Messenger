package com.qxlabai.presentation.xmpp

import android.util.Log
import com.qxlabai.domain.events.Events
import com.qxlabai.domain.interactors.xmpp.AuthenticateUseCase
import com.qxlabai.domain.interactors.xmpp.ConnectXmppUseCase
import com.qxlabai.domain.interactors.xmpp.FetchUserIdUseCase
import com.qxlabai.domain.interactors.xmpp.entity.Credentials
import com.qxlabai.presentation.core.Middleware
import com.qxlabai.presentation.core.Store
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class XmppMiddleware @Inject constructor(
    private val connectXmppUseCase: ConnectXmppUseCase,
    private val authenticateUseCase: AuthenticateUseCase,
    private val fetchUserId: FetchUserIdUseCase
) : Middleware<XmppState, XmppAction> {

    companion object {
        private val TAG = XmppMiddleware::class.java.simpleName
    }

    override suspend fun process(
        action: XmppAction,
        currentState: XmppState,
        store: Store<XmppState, XmppAction>
    ) {
        when (action) {
            is XmppAction.BindService -> TODO()
            XmppAction.Connect -> connectWithXmpp(store)
            is XmppAction.Authenticate -> authenticate(store, action)
            is XmppAction.FetchUserId -> fetchUserId(store)
            else -> Unit
        }
    }

    private suspend fun fetchUserId(store: Store<XmppState, XmppAction>) {

        try {
            fetchUserId.invoke(Unit).collectLatest { result ->
                when (result) {
                    is Events.Error -> store.dispatch(
                        XmppAction.OnError(
                            errorMessage = result.message
                        )
                    )

                    is Events.Loading -> store.dispatch(XmppAction.Processing(result.message))
                    is Events.Success -> store.dispatch(XmppAction.OnUserId(userId = result.data))
                }
            }
        } catch (exception: Exception) {
            return store.dispatch(XmppAction.OnError(exception.message.toString()))
        }
    }

    private suspend fun authenticate(
        store: Store<XmppState, XmppAction>,
        action: XmppAction.Authenticate
    ) {
        try {
            authenticateUseCase.invoke(
                Credentials(
                    action.uuid,
                    action.passcode
                )
            ).collectLatest { result ->
                when (result) {
                    is Events.Error -> store.dispatch(
                        XmppAction.OnError(
                            errorMessage = result.message
                        )
                    )

                    is Events.Loading -> store.dispatch(XmppAction.Processing(result.message))
                    is Events.Success -> store.dispatch(XmppAction.OnSuccess(XmppAction.STAGE.AUTHENTICATION))
                }
            }
        } catch (exception: Exception) {
            return store.dispatch(XmppAction.OnFailure(XmppAction.STAGE.AUTHENTICATION))
        }
    }

    private suspend fun connectWithXmpp(store: Store<XmppState, XmppAction>) {
        try {
            connectXmppUseCase.invoke(Unit).collectLatest { result ->
                when (result) {
                    is Events.Error -> {
                        Log.e(TAG, result.message, result.cause)
                        store.dispatch(XmppAction.OnError(result.message))
                    }

                    is Events.Loading -> {
                        Log.i(TAG, result.message)
                        store.dispatch(XmppAction.Processing(result.message))
                    }

                    is Events.Success -> {
                        Log.i(TAG, result.data.toString())
                        if (result.data) {
                            store.dispatch(XmppAction.OnSuccess(XmppAction.STAGE.CONNECTION))
                        } else store.dispatch(XmppAction.OnFailure(XmppAction.STAGE.CONNECTION))
                    }
                }
            }
        } catch (exception: Exception) {
            store.dispatch(XmppAction.OnFailure(XmppAction.STAGE.CONNECTION))
        }
    }
}