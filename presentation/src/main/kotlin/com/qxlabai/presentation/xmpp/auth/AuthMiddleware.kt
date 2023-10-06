package com.qxlabai.presentation.xmpp.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import com.qxlabai.domain.events.Events
import com.qxlabai.domain.interactors.authentication.FetchSavedCredentialsUseCase
import com.qxlabai.domain.interactors.authentication.UpdateCredentialsUseCase
import com.qxlabai.domain.interactors.xmpp.entity.Credentials
import com.qxlabai.presentation.core.Middleware
import com.qxlabai.presentation.core.Store
import com.qxlabai.presentation.xmpp.services.XmppService
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class AuthMiddleware @Inject constructor(
    private val context: Context,
    private val updateCredentialsUseCase: UpdateCredentialsUseCase,
    private val fetchSavedCredentialsUseCase: FetchSavedCredentialsUseCase
) : Middleware<AuthState, AuthAction> {


    override suspend fun process(
        action: AuthAction,
        currentState: AuthState,
        store: Store<AuthState, AuthAction>
    ) {
        when (action) {

//            AuthAction.Authenticated -> processAuthentication(currentState)
//            AuthAction.ValidateAuth -> validateAuth(store)
//            is AuthAction.Authenticate -> authenticate()
            else -> Unit
        }
    }

//    private suspend fun validateAuth(store: Store<AuthState, AuthAction>) {
//        fetchSavedCredentialsUseCase.invoke(Unit).last().let { event ->
//            when (event) {
//                is Events.Error -> store.dispatch(AuthAction.AuthError(event.message))
//                is Events.Loading -> store.dispatch(AuthAction.ValidatingAuth(event.message))
//                is Events.Success -> {
//                    val credentials = event.data
//                    if (credentials.passcode.isNotEmpty() && credentials.username.isNotEmpty()) {
//                        store.dispatch(AuthAction.Authenticate(credentials))
//                    } else store.dispatch(AuthAction.NotAuthenticated)
//                }
//            }
//        }
//    }

    private suspend fun processAuthentication(currentState: AuthState) {
        try {
            updateCredentialsUseCase.invoke(
                Credentials(
                    username = currentState.uuid,
                    passcode = currentState.passcode
                )
            ).collectLatest {
                Log.e("AUTH", it.toString())
            }
        } catch (exception: Exception) {

        }
    }

    private fun authenticate() {
        context.startService(Intent(context, XmppService::class.java).also {
            it.action = XmppService.COMMAND_AUTH
        })
    }


}