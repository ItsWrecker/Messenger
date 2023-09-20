package com.qxlabai.messenger.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.qxlabai.messenger.screeens.ConversationScreen
import com.qxlabai.messenger.screeens.LockScreen

@Composable
fun SetupNavigation(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Destinations.ConversationScreen.route) {
        composable(Destinations.LockScreen.route){
            LockScreen(navHostController = navHostController)
        }

        composable(Destinations.ConversationScreen.route){
            ConversationScreen(navHostController = navHostController)
        }
    }
}