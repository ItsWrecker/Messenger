package com.qxlabai.presentation.chat

import android.net.Uri
import com.qxlabai.presentation.navigation.MessengerNavigationDestination
import com.qxlabai.presentation.navigation.NavigationParameters

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