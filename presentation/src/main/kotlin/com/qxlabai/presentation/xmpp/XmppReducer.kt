package com.qxlabai.presentation.xmpp

import com.qxlabai.presentation.core.Reducer
import javax.inject.Inject

class XmppReducer @Inject constructor(

) : Reducer<XmppState, XmppAction> {

    override suspend fun reduce(currentState: XmppState, action: XmppAction): XmppState {
        return when (action) {
            is XmppAction.Processing -> {
                currentState.copy(
                    isLoading = true,
                    status = action.message,
                    error = null
                )
            }

            is XmppAction.OnError -> currentState.copy(error = action.errorMessage)
            is XmppAction.OnFailure -> currentState.copy(
                error = when (action.state) {
                    XmppAction.STAGE.CONNECTION -> "Error while establishing the connection, please check inner connection and retry!!"
                    XmppAction.STAGE.AUTHENTICATION -> "Please, enter valid UUID and Password!!"
                }
            )

            is XmppAction.OnSuccess -> {
                currentState.copy(
                    isLoading = false,
                    error = null,
                    state = XmppAction.STAGE.AUTHENTICATION,
                    isConnectionEstablished = true,
                    isAuthenticated = when (action.state) {
                        XmppAction.STAGE.CONNECTION -> currentState.isAuthenticated
                        XmppAction.STAGE.AUTHENTICATION -> true
                    }
                )
            }

            is XmppAction.OnUserId -> currentState.copy(
                userId = action.userId,
                isLoading = false,
                error = null
            )

            else -> currentState
        }
    }
}