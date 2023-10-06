package com.qxlabai.presentation.xmpp.connection

import com.qxlabai.presentation.core.State

data class XmppState(
    val progressMessage: String? = null,
    val serviceStarted: Boolean = false,
    val isConnectionEstablished: Boolean = false,
    val isConnecting: Boolean = false,
    val isAuthenticated: Boolean = false,
    val showProgress: Boolean = false
    ) : State