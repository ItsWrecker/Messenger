package com.qxlabai.presentation.xmpp

import com.qxlabai.presentation.core.State

data class XmppState(
    val isConnectionEstablished: Boolean = false
): State