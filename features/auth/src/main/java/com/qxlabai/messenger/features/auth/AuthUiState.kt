package com.qxlabai.messenger.features.auth

sealed interface AuthUiState {
    object Idle : AuthUiState

    object Loading : AuthUiState

    object Success : AuthUiState

    data class Error(val message: String) : AuthUiState
}