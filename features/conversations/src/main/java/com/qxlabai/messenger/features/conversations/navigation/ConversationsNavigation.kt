package com.qxlabai.messenger.features.conversations.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.qxlabai.messenger.core.navigation.MessengerNavigationDestination
import com.qxlabai.messenger.features.conversations.ConversationsRoute

object ConversationsDestination : MessengerNavigationDestination {
    override val route = "conversations_route"
    override val destination = "conversations_destination"
}

fun NavGraphBuilder.conversationsGraph(
    navigateToChat: (String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit
) {
    navigation(
        route = ConversationsDestination.route,
        startDestination = ConversationsDestination.destination
    ) {
        composable(route = ConversationsDestination.destination) {
            ConversationsRoute(
                navigateToChat = navigateToChat
            )
        }
        nestedGraphs()
    }
}
