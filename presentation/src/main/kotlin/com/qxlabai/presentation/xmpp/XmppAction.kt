package com.qxlabai.presentation.xmpp

import android.content.Context
import com.qxlabai.presentation.core.Action

sealed interface XmppAction : Action {

    enum class STAGE {
        CONNECTION,
        AUTHENTICATION
    }

    object Connect : XmppAction

    data class Authenticate(val uuid: String, val passcode: String) : XmppAction
    data class BindService(val context: Context) : XmppAction

    data class OnError(val errorMessage: String) : XmppAction
    data class ConnectingWithXmpp(val message: String) : XmppAction

    data class OnSuccess(val state: STAGE) : XmppAction
    data class OnFailure(val state: STAGE) : XmppAction

    data class Processing(val message: String) : XmppAction
    object FetchUserId : XmppAction
    data class OnUserId(val userId: String): XmppAction
}