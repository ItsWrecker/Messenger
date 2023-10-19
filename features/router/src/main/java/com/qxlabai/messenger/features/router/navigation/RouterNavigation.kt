package com.qxlabai.messenger.features.router.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.qxlabai.messenger.core.navigation.MessengerNavigationDestination
import com.qxlabai.messenger.features.router.RouterRoute

object RouterDestination : MessengerNavigationDestination {
    override val route = "router_route"
    override val destination = "router_destination"
}

fun NavGraphBuilder.routerGraph(
    navigateToAuth: () -> Unit,
    navigateToConversations: () -> Unit
) {
    composable(
        route = RouterDestination.route,
    ) {
        RouterRoute(
            navigateToAuth = navigateToAuth,
            navigateToConversations = navigateToConversations
        )
    }
}
