package com.qxlabai.presentation.router

import com.qxlabai.presentation.core.State

sealed interface RouterUiState : State {
    object Loading : RouterUiState
    object UserAvailable : RouterUiState
    object AuthRequired : RouterUiState

}