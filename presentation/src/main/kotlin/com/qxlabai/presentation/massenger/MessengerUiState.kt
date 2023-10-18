package com.qxlabai.presentation.massenger

import com.qxlabai.presentation.core.State


sealed interface MessengerUiState : State {
    object Idle : MessengerUiState

    object Connected : MessengerUiState

    object Connecting : MessengerUiState
}