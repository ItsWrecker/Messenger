package com.qxlabai.presentation.xmpp.profile

import com.qxlabai.presentation.core.State

data class ProfileState(
    val isLoading: Boolean = false,
    val userName: String = "",
) : State