package com.qxlabai.messenger.ui

import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import com.qxlabai.messenger.core.navigation.NavigationParameters
import com.qxlabai.messenger.features.auth.navigation.AuthDestination
import com.qxlabai.messenger.features.chat.navigation.ChatDestination
import com.qxlabai.messenger.features.contacts.navigation.ContactsDestination
import com.qxlabai.messenger.features.conversations.navigation.ConversationsDestination
import com.qxlabai.messenger.features.router.navigation.RouterDestination
import com.qxlabai.messenger.navigation.TopLevelDestination
import com.qxlabai.messenger.R
@Composable
fun rememberMessengerAppState(
    navController: NavHostController = rememberNavController()
): MessengerAppState {
    return remember(navController) {
        MessengerAppState(navController)
    }
}

@Stable
class MessengerAppState(
    val navController: NavHostController
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val shouldShowBottomBar: Boolean
        @Composable get() {
            val route = currentDestination?.route
            return route != null &&
                route != RouterDestination.route &&
                route != AuthDestination.route &&
                route != ChatDestination.route
        }

    val shouldShowConnecting: Boolean
        @Composable get() {
            val route = currentDestination?.route
            return route != null &&
                route != RouterDestination.route &&
                route != AuthDestination.route
        }

    /**
     * Top level destinations to be used in the BottomBar
     */
    val topLevelDestinations = listOf(
        TopLevelDestination(
            route = ConversationsDestination.route,
            destination = ConversationsDestination.destination,
            icon = Filled.Chat,
            iconTextId = R.string.conversations
        ),
        TopLevelDestination(
            route = ContactsDestination.route,
            destination = ContactsDestination.destination,
            icon = Filled.Contacts,
            iconTextId = R.string.contacts
        ),
//        TopLevelDestination(
//            route = SettingsDestination.route,
//            destination = SettingsDestination.destination,
//            icon = Filled.Settings,
//            iconTextId = R.string.settings
//        )
    )

    fun navigate(parameters: NavigationParameters) {
        if (parameters.destination is TopLevelDestination) {
            navController.navigate(parameters.route ?: parameters.destination.route) {
                popUpTo(ConversationsDestination.destination) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        } else {
            navController.navigate(parameters.route ?: parameters.destination.route) {
                parameters.popUpToInclusive?.let {
                    popUpTo(it.route) { inclusive = true }
                }
            }
        }
    }

    fun onBackClick() {
        navController.popBackStack()
    }
}
