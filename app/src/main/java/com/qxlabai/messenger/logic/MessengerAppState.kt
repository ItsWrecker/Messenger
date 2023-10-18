package com.qxlabai.messenger.logic

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.qxlabai.messenger.contacts.ContactsDestination
import com.qxlabai.messenger.conversation.ConversationDestination
import com.qxlabai.messenger.navigation.TopLevelDestination
import com.qxlabai.messenger.router.RouterDestination
import com.qxlabai.presentation.auth.AuthDestination
import com.qxlabai.presentation.navigation.NavigationParameters


@Composable
fun rememberMessengerAppState(
    navController: NavHostController = rememberNavController()
): MessengerAppState = remember(navController) {
    MessengerAppState(navController)
}


@Stable
class MessengerAppState(val navController: NavHostController) {

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val shouldShowBottomBar: Boolean
        @Composable get() {
            val route = currentDestination?.route
            return route != null
                    && route != RouterDestination.route
                    && route != AuthDestination.route
        }

    val shouldShowConnecting: Boolean
        @Composable get() {
            val route = currentDestination?.route
            return route != null &&
                    route != RouterDestination.route &&
                    route != AuthDestination.route
        }

    val topLevelDestination = listOf(
        TopLevelDestination(
            route = ConversationDestination.route,
            destination = ConversationDestination.destination,
            icon = Icons.Filled.MailOutline,
            iconText = "Conversations"
        ),
        TopLevelDestination(
            route = ContactsDestination.route,
            destination = ContactsDestination.destination,
            icon = Icons.Filled.AccountBox,
            iconText = "Contacts"
        )


    )

    fun navigate(params: NavigationParameters) {
        if (params.destination is TopLevelDestination) {
            navController.navigate(params.route ?: params.destination.route) {
                popUpTo(ConversationDestination.destination) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        } else {
//            navController.navigate(params.route ?: params.destination.route) {
//                params.popUpToInclusive?.let {
//                    popUpTo(it.route) {
//                        inclusive = true
//                    }
//                }
//            }
        }
    }

    fun onBackPress() {
        navController.popBackStack()
    }


}