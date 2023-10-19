package com.qxlabai.messenger.core.data.model

import com.qxlabai.messenger.core.model.data.ThemeConfig
import com.qxlabai.messenger.core.datastore.PreferencesThemeConfig
import com.qxlabai.messenger.core.datastore.UserPreferences.DarkConfig
import com.qxlabai.messenger.core.datastore.UserPreferences.ThemeBranding

fun ThemeConfig.asPreferences() = PreferencesThemeConfig(
    themeBranding = ThemeBranding.valueOf(themeBranding.name),
    darkConfig = DarkConfig.valueOf(darkConfig.name)
)
