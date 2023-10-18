package com.qxlabai.messenger.contacts

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.qxlabai.presentation.navigation.MessengerNavigationDestination

object ContactsDestination: MessengerNavigationDestination {

    override val route = "contacts_route"
    override val destination = "contacts_destination"
}

fun NavGraphBuilder.contactsGraph(
    navigateToChat: (String) -> Unit,
) {
    composable(
        route = ContactsDestination.route,
    ) {
        ContactsRoute(
            navigateToChat = navigateToChat
        )
    }
}
