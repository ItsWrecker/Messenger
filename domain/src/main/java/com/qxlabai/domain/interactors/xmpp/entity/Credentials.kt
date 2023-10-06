package com.qxlabai.domain.interactors.xmpp.entity

import kotlinx.serialization.Serializable

@Serializable
data class Credentials(
    val username: String,
    val passcode: String
):java.io.Serializable
