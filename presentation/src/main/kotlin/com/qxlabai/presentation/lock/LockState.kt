package com.qxlabai.presentation.lock

import com.qxlabai.presentation.core.State

data class LockState(
    val isVerified: Boolean,
    val error: String? = null,
    val status: String? = null
) : State