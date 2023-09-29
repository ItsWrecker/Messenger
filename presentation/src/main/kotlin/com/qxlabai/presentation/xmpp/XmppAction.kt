package com.qxlabai.presentation.xmpp

import com.qxlabai.presentation.core.Action

sealed interface XmppAction: Action {
    object Connect: XmppAction
}