package com.qxlabai.presentation.xmpp

import com.qxlabai.presentation.core.State

data class XmppState(
    val isLoading: Boolean = false,
    val isConnectionEstablished: Boolean = false,
    val status: String = "Connecting to the XMPP Service",
    val error: String? = null,
    val state: XmppAction.STAGE = XmppAction.STAGE.CONNECTION,
    val isAuthenticated: Boolean = false,
    val userId: String? = null
) : State