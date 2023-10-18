package com.qxlabai.messenger.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.qxlabai.messenger.chat.ChatDestination
import com.qxlabai.messenger.chat.ChatDestination.createNavigationParameters
import com.qxlabai.messenger.chat.chatGraph
import com.qxlabai.messenger.contacts.contactsGraph
import com.qxlabai.messenger.conversation.ConversationDestination
import com.qxlabai.messenger.conversation.conversationGraph
import com.qxlabai.messenger.router.RouterDestination
import com.qxlabai.messenger.router.routeGraph
import com.qxlabai.presentation.auth.AuthDestination
import com.qxlabai.presentation.auth.authGraph
import com.qxlabai.presentation.navigation.NavigationParameters


@Composable
fun MessengerNavHost(
    navController: NavHostController,
    onNavigateToDestination: (NavigationParameters) -> Unit,
    onExitChat: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = RouterDestination.route
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        routeGraph(
            navigateToAuth = {
                onNavigateToDestination(
                    NavigationParameters(
                        destination = AuthDestination,
                        popUpToInclusive = RouterDestination
                    )
                )
            },
            navigateConversation = {
                onNavigateToDestination(
                    NavigationParameters(
                        destination = ConversationDestination,
                        popUpToInclusive = RouterDestination
                    )
                )
            }
        )

        authGraph(
            navigateToConversations = {
                onNavigateToDestination(
                    NavigationParameters(
                        destination = ConversationDestination,
                        popUpToInclusive = AuthDestination
                    )
                )
            }
        )
        conversationGraph(
            navigateToChat = {
                onNavigateToDestination(ChatDestination.createNavigationParameters(it))
            },
            nestedGraphs = {
                chatGraph(
                    onBackClick = {
                        onExitChat(it)
                        onBackClick()
                    }
                )
            }

        )
        contactsGraph(
            navigateToChat = {
                onNavigateToDestination(createNavigationParameters(it))
            }
        )
    }
}