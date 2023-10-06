package com.qxlabai.presentation.xmpp.profile

import com.qxlabai.presentation.core.Action

sealed interface ProfileAction: Action {


    object FetchProfile: ProfileAction

    data class Profile(val user: String): ProfileAction
}