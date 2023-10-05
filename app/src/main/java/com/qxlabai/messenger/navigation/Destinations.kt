package com.qxlabai.messenger.navigation

sealed class Destinations(val route: String) {
    object LockScreen : Destinations("LOCK_SCREEN")
    object ConversationScreen : Destinations("CONVERSATION_SCREEN")
    object ConnectionScreen : Destinations("CONNECTION_SCREEN")
    object AuthenticationScreen : Destinations("AUTHENTICATION_SCREEN")
    object ProfileScreen : Destinations("PROFILE_SCREEN")
}