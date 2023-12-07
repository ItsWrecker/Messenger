package com.qxlabai.messenger.core.datastore.lock

import kotlinx.serialization.Serializable

@Serializable
data class LockPreferences(
    val passcode: String? = ""
)