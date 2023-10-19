package com.qxlabai.messenger.core.datastore

import com.qxlabai.messenger.core.model.data.DarkConfig
import com.qxlabai.messenger.core.model.data.ThemeBranding
import com.qxlabai.messenger.core.model.data.ThemeConfig

data class PreferencesThemeConfig(
    val themeBranding: UserPreferences.ThemeBranding,
    val darkConfig: UserPreferences.DarkConfig
)

fun PreferencesThemeConfig.asExternalModel() = ThemeConfig(
    themeBranding = ThemeBranding.valueOf(themeBranding.name),
    darkConfig = DarkConfig.valueOf(darkConfig.name)
)
