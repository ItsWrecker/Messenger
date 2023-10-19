package com.qxlabai.messenger.core.navigation

data class NavigationParameters(
    val destination: MessengerNavigationDestination,
    val route: String? = null,
    val popUpToInclusive: MessengerNavigationDestination? = null
)
