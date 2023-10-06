package com.qxlabai.presentation.xmpp.auth

import com.qxlabai.presentation.core.Action

sealed interface AuthAction : Action {

    data class UpdateUUID(
        val uuid: String
    ): AuthAction

    data class UpdatePasscode(
        val passcode: String
    ): AuthAction

    object Authenticate: AuthAction

    object Authenticated: AuthAction



}