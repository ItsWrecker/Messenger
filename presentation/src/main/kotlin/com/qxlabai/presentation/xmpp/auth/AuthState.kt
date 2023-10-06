package com.qxlabai.presentation.xmpp.auth

import com.qxlabai.presentation.core.State

data class AuthState(
    val isAuthenticated: Boolean = false,
    val uuid: String = "",
    val passcode: String = "",
    val isAuthenticating: Boolean = false
): State