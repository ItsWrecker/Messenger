package com.qxlabai.messenger.conversation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.qxlabai.presentation.navigation.MessengerNavigationDestination

object ConversationDestination: MessengerNavigationDestination {

    override val route = "conversations_route"
    override val destination = "conversations_destination"


}

fun NavGraphBuilder.conversationGraph(
    navigateToChat: (String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit
){
    navigation(
        route = ConversationDestination.route,
        startDestination = ConversationDestination.destination
    ){
        composable(route = ConversationDestination.destination){

        }
        nestedGraphs()
    }
}