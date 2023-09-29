package com.qxlabai.messenger.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.qxlabai.messenger.screeens.ConnectionScreen
import com.qxlabai.messenger.screeens.ConversationScreen
import com.qxlabai.messenger.screeens.LockScreen
import com.qxlabai.messenger.screeens.ProfileScreen

@Composable
fun SetupNavigation(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Destinations.ConnectionScreen.route) {
        composable(Destinations.LockScreen.route){
            LockScreen(navHostController = navHostController)
        }

        composable(Destinations.ConversationScreen.route){
            ConversationScreen(navHostController = navHostController)
        }

        composable(Destinations.ConnectionScreen.route){
            ConnectionScreen(navHostController = navHostController)
        }

        composable(Destinations.ConnectionScreen.route){
            ProfileScreen(navHostController = navHostController)
        }
    }
}