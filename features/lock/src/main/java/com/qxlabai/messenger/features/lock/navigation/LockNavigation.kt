package com.qxlabai.messenger.features.lock.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.qxlabai.messenger.core.navigation.MessengerNavigationDestination
import com.qxlabai.messenger.features.lock.LockRoute

object LockDestination : MessengerNavigationDestination {

    override val route = "lock_route"
    override val destination = "lock_destination"


}

fun NavGraphBuilder.lockGraph(
    navigateToConversations: () -> Unit,
    navigateToAuth: () -> Unit,
) {
    composable(route = LockDestination.route) {
        LockRoute(navigateToConversations = navigateToConversations, navigateToAuth = navigateToAuth)
    }
}