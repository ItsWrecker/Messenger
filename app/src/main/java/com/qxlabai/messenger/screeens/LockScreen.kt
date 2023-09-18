package com.qxlabai.messenger.screeens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.qxlabai.messenger.navigation.Destinations

@Composable
fun LockScreen(navHostController: NavHostController) {
    Text(text = "LockScreen", modifier = Modifier.clickable {
        navHostController.navigate(Destinations.ConversationScreen.route)
    })
}