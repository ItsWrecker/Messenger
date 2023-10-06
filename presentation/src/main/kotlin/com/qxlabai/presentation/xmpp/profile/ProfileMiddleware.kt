package com.qxlabai.presentation.xmpp.profile

import android.content.Context
import android.content.Intent
import com.qxlabai.presentation.core.Middleware
import com.qxlabai.presentation.core.Store
import com.qxlabai.presentation.xmpp.services.XmppService
import javax.inject.Inject

class ProfileMiddleware @Inject constructor(
    private val context: Context
) : Middleware<ProfileState, ProfileAction> {
    override suspend fun process(
        action: ProfileAction,
        currentState: ProfileState,
        store: Store<ProfileState, ProfileAction>
    ) {

        when (action) {
            is ProfileAction.FetchProfile -> fetchProfile()
            else -> Unit
        }
    }

    private fun fetchProfile() {
        context.startService(Intent(context, XmppService::class.java).also {
            it.action = XmppService.COMMAND_PROFILE
        })
    }
}