package com.qxlabai.messenger.features.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.qxlabai.messenger.core.navigation.MessengerNavigationDestination
import com.qxlabai.messenger.features.auth.AuthRoute

object AuthDestination : MessengerNavigationDestination {
    override val route = "auth_route"
    override val destination = "auth_destination"
}

fun NavGraphBuilder.authGraph(
    navigateToLockScreen: () -> Unit
) {
    composable(
        route = AuthDestination.route,
    ) {
        AuthRoute(navigateToLockScreen = navigateToLockScreen)
    }
}
