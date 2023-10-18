package com.qxlabai.messenger.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.qxlabai.presentation.navigation.MessengerNavigationDestination

data class TopLevelDestination(
    override val route: String,
    override val destination: String,
    val icon: ImageVector,
    val iconText: String
) : MessengerNavigationDestination