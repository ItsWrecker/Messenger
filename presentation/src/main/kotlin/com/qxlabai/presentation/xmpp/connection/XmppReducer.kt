package com.qxlabai.presentation.xmpp.connection

import com.qxlabai.presentation.core.Reducer
import javax.inject.Inject

class XmppReducer @Inject constructor(

) : Reducer<XmppState, XmppAction> {
    override suspend fun reduce(currentState: XmppState, action: XmppAction): XmppState {
        return when (action) {

            is XmppAction.Connecting -> {
                currentState.copy(
                    isConnecting = true,
                    progressMessage = "Connecting to XMPP Server"
                )
            }

            is XmppAction.Connected -> {
                currentState.copy(
                    isConnecting = false,
                    isConnectionEstablished = true
                )
            }

            is XmppAction.Authenticated -> {
                currentState.copy(
                    isAuthenticated = true,
                    isConnectionEstablished = true,
                    isConnecting = false
                )
            }

            is XmppAction.ConnectionClosed -> {
                currentState.copy(
                    isConnecting = true,
                    isConnectionEstablished = false,
                    isAuthenticated = false
                )
            }

            is XmppAction.ConnectionError -> {
                currentState.copy(
                    isAuthenticated = false,
                    isConnectionEstablished = false
                )
            }
            is XmppAction.ServiceStart -> currentState.copy(
                progressMessage = "Starting XMPP Service"
            )
            else -> currentState
        }
    }
}