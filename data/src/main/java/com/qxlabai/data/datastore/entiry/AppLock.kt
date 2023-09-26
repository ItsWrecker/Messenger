package com.qxlabai.data.datastore.entiry

import kotlinx.serialization.Serializable

@Serializable
data class AppLock(
    val passcode: String
)