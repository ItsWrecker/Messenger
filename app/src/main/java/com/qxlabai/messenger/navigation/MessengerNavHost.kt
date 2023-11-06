package com.qxlabai.messenger.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.qxlabai.messenger.core.navigation.NavigationParameters
import com.qxlabai.messenger.features.auth.navigation.AuthDestination
import com.qxlabai.messenger.features.auth.navigation.authGraph
import com.qxlabai.messenger.features.chat.navigation.ChatDestination.createNavigationParameters
import com.qxlabai.messenger.features.chat.navigation.chatGraph
import com.qxlabai.messenger.features.contacts.navigation.contactsGraph
import com.qxlabai.messenger.features.conversations.navigation.ConversationsDestination
import com.qxlabai.messenger.features.conversations.navigation.conversationsGraph
import com.qxlabai.messenger.features.lock.navigation.LockDestination
import com.qxlabai.messenger.features.lock.navigation.lockGraph
import com.qxlabai.messenger.features.router.navigation.RouterDestination
import com.qxlabai.messenger.features.router.navigation.routerGraph
import com.qxlabai.messenger.features.settings.navigation.SettingsDestination
import com.qxlabai.messenger.features.settings.navigation.settingsGraph

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
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        routerGraph(
            navigateToAuth = {
                onNavigateToDestination(
                    NavigationParameters(
                        destination = AuthDestination,
                        popUpToInclusive = RouterDestination
                    )
                )
            },
            navigateToConversations = {
                onNavigateToDestination(
                    NavigationParameters(
                        destination = ConversationsDestination,
                        popUpToInclusive = RouterDestination
                    )
                )
            },
            navigateToLock = {
                onNavigateToDestination(
                    NavigationParameters(
                        destination = LockDestination,
                        popUpToInclusive = null
                    )
                )
            }
        )

        lockGraph(navigateToConversations = {
            onNavigateToDestination(
                NavigationParameters(
                    destination = ConversationsDestination,
                    popUpToInclusive = LockDestination
                )
            )
        })
        authGraph(
            navigateToConversations = {
                onNavigateToDestination(
                    NavigationParameters(
                        destination = ConversationsDestination,
                        popUpToInclusive = AuthDestination
                    )
                )
            }
        )
        conversationsGraph(
            navigateToChat = {
                onNavigateToDestination(createNavigationParameters(it))
            },
            navigateToSettings = {
                onNavigateToDestination(
                    NavigationParameters(destination = SettingsDestination)
                )
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
        settingsGraph {
            navController.popBackStack()
        }
    }
}
