package com.qxlabai.messenger.router

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.qxlabai.presentation.navigation.MessengerNavigationDestination

object RouterDestination : MessengerNavigationDestination {
    override val route = "router_route"
    override val destination = "router_destination"
}

fun NavGraphBuilder.routeGraph(
    navigateToAuth: () -> Unit,
    navigateConversation: () -> Unit
) {
    composable(route = RouterDestination.route) {
        RouterRoute(navigateToAuth = navigateToAuth, navigateToConversation = navigateConversation)
    }
}


