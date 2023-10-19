package com.qxlabai.messenger.core.data.model

import com.qxlabai.messenger.core.model.data.ConnectionStatus
import com.qxlabai.messenger.core.datastore.PreferencesConnectionStatus

fun ConnectionStatus.asPreferences() = PreferencesConnectionStatus(
    availability = availability,
    authenticated = authenticated
)
