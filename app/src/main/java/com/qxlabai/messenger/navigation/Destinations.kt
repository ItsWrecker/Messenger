package com.qxlabai.messenger.navigation

sealed class Destinations (val route: String){
    object LockScreen: Destinations("LOCK_SCREEN")
    object ConversationScreen: Destinations("CONVERSATION_SCREEN")
}