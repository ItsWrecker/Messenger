package com.qxlabai.messenger.features.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qxlabai.messenger.features.router.RouterUiState.AuthRequired
import com.qxlabai.messenger.features.router.RouterUiState.UserAvailable

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun RouterRoute(
    navigateToAuth: () -> Unit,
    navigateToConversations: () -> Unit,
    viewModel: RouterViewModel = hiltViewModel(),
    navigateToLock: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RouterScreen(
        uiState = uiState,
        navigateToAuth = navigateToAuth,
        navigateToLock = navigateToLock,
        navigateToConversations = navigateToConversations
    )
}

@Composable
fun RouterScreen(
    uiState: RouterUiState,
    navigateToAuth: () -> Unit,
    navigateToLock: () -> Unit,
    navigateToConversations: () -> Unit,
) {

    LaunchedEffect(uiState) {
        when (uiState) {
            is UserAvailable -> navigateToLock()
            is AuthRequired -> navigateToAuth()
            else -> {}
        }
    }
}
