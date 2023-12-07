package com.qxlabai.messenger.features.contacts.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.qxlabai.messenger.core.navigation.MessengerNavigationDestination
import com.qxlabai.messenger.features.contacts.ContactsRoute

object ContactsDestination : MessengerNavigationDestination {
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
