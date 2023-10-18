package com.qxlabai.presentation.navigation

data class NavigationParameters(
    val destination: MessengerNavigationDestination,
    val route: String? = null,
    val popUpToInclusive: MessengerNavigationDestination? = null
)
