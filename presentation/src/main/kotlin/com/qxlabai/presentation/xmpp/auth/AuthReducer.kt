package com.qxlabai.presentation.xmpp.auth

import com.qxlabai.presentation.core.Reducer
import javax.inject.Inject

class AuthReducer @Inject constructor(

) : Reducer<AuthState, AuthAction> {

    override suspend fun reduce(currentState: AuthState, action: AuthAction): AuthState {

        return when (action) {
            AuthAction.Authenticate -> currentState.copy(
                isAuthenticating = true
            )

            is AuthAction.UpdatePasscode -> currentState.copy(
                passcode = action.passcode
            )

            is AuthAction.UpdateUUID -> currentState.copy(uuid = action.uuid)

            is AuthAction.Authenticated -> currentState.copy(
                isAuthenticating = false,
                isAuthenticated = true
            )
        }
    }
}