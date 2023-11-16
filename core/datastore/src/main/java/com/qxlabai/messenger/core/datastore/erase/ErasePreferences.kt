package com.qxlabai.messenger.core.datastore.erase

import androidx.annotation.Keep
import kotlinx.serialization.Serializable


@Keep
@Serializable
data class ErasePreferences(
    val incorrectPassCount: Int = 0
)