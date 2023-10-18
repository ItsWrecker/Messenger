package com.qxlabai.presentation.contacts

import com.qxlabai.domain.entity.Contact
import com.qxlabai.presentation.core.State

sealed interface ContactsUiState : State {
    object Loading : ContactsUiState

    data class Success(val contacts: List<Contact>) : ContactsUiState
}