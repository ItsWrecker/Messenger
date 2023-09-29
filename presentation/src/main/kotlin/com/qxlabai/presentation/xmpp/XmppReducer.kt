package com.qxlabai.presentation.xmpp

import com.qxlabai.presentation.core.Reducer
import javax.inject.Inject

class XmppReducer @Inject constructor(

) : Reducer<XmppState, XmppAction> {

    override suspend fun reduce(currentState: XmppState, action: XmppAction): XmppState {
        return currentState
    }
}