package com.qxlabai.messenger.core.datastore

import com.qxlabai.messenger.core.model.data.ConnectionStatus

data class PreferencesConnectionStatus(
    val availability: Boolean = false,
    val authenticated: Boolean = false
)

fun PreferencesConnectionStatus.asExternalModel() = ConnectionStatus(
    availability = availability,
    authenticated = authenticated
)
