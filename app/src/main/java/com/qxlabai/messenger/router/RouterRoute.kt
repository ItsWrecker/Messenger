package com.qxlabai.messenger.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.qxlabai.presentation.router.RouterUiState
import com.qxlabai.presentation.router.RouterViewModel

@Composable
fun RouterRoute(
    navigateToAuth: () -> Unit,
    navigateToConversation: () -> Unit,
    viewModel: RouterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = uiState) {
        when (uiState) {
            RouterUiState.AuthRequired -> navigateToAuth()
            RouterUiState.Loading -> {}
            RouterUiState.UserAvailable -> navigateToConversation()
        }
    }
}