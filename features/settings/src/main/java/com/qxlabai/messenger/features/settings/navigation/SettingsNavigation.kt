package com.qxlabai.messenger.features.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.qxlabai.messenger.core.navigation.MessengerNavigationDestination
import com.qxlabai.messenger.features.settings.SettingsRoute

object SettingsDestination : MessengerNavigationDestination {
    override val route = "settings_route"
    override val destination = "settings_destination"
}

fun NavGraphBuilder.settingsGraph(
    onBackClick: () -> Unit
) {
    composable(
        route = SettingsDestination.route,


        ) {
        SettingsRoute(onBackClick = onBackClick)
    }
}
