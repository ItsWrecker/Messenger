package com.qxlabai.messenger.features.chat.navigation

import android.net.Uri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.qxlabai.messenger.core.navigation.MessengerNavigationDestination
import com.qxlabai.messenger.core.navigation.NavigationParameters
import com.qxlabai.messenger.features.chat.ChatRoute

object ChatDestination : MessengerNavigationDestination {
    const val contactJidArg = "contactJid"
    override val route = "chat_route/{$contactJidArg}"
    override val destination = "chat_destination"

    /**
     * Creates destination route navigation parameters for a contactJid
     * that could include special characters
     */
    fun createNavigationParameters(contactId: String): NavigationParameters =
        NavigationParameters(
            destination = ChatDestination,
            route = createNavigationRoute(contactId)
        )

    private fun createNavigationRoute(contactJidArg: String): String {
        val encodedJid = Uri.encode(contactJidArg)
        return "chat_route/$encodedJid"
    }
}

fun NavGraphBuilder.chatGraph(
    onBackClick: (String) -> Unit
) {
    composable(
        route = ChatDestination.route,
        arguments = listOf(
            navArgument(ChatDestination.contactJidArg) { type = NavType.StringType }
        )
    ) {
        ChatRoute(onBackClick = onBackClick)
    }
}
