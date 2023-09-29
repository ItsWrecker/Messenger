package com.qxlabai.presentation.xmpp

import com.qxlabai.presentation.core.Middleware
import com.qxlabai.presentation.core.Store
import javax.inject.Inject

class XmppMiddleware @Inject constructor(

) : Middleware<XmppState, XmppAction>{

    override suspend fun process(
        action: XmppAction,
        currentState: XmppState,
        store: Store<XmppState, XmppAction>
    ) {

    }
}