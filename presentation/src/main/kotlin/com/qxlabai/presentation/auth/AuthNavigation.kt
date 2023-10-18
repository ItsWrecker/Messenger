package com.qxlabai.presentation.auth

import androidx.navigation.NavGraphBuilder
import com.qxlabai.presentation.navigation.MessengerNavigationDestination


object AuthDestination : MessengerNavigationDestination {
    override val destination: String
        get() = "auth_destination"
    override val route: String
        get() = "auth_rout"
}



fun NavGraphBuilder.authGraph(
    navigateToConversations: () -> Unit
){

}