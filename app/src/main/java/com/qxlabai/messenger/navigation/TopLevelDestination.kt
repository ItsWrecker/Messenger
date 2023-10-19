package com.qxlabai.messenger.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.qxlabai.messenger.core.navigation.MessengerNavigationDestination

/**
 * Type for the top level destinations in the application. Each of these destinations
 * can contain one or more screens (based on the window size). Navigation from one screen to the
 * next within a single destination will be handled directly in composables.
 */
data class TopLevelDestination(
    override val route: String,
    override val destination: String,
    val icon: ImageVector,
    val iconTextId: Int
) : MessengerNavigationDestination
