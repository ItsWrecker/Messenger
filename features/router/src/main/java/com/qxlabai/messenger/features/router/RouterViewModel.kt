package com.qxlabai.messenger.features.router

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.qxlabai.messenger.core.data.repository.PreferencesRepository
import com.qxlabai.messenger.features.router.RouterUiState.AuthRequired
import com.qxlabai.messenger.features.router.RouterUiState.Loading
import com.qxlabai.messenger.features.router.RouterUiState.UserAvailable
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class RouterViewModel @Inject constructor(
    preferencesRepository: PreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<RouterUiState> = preferencesRepository.getAccount()
        .map { account ->
            if (account.alreadyLoggedIn) {
                UserAvailable
            } else {
                AuthRequired
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Loading
        )
}

sealed interface RouterUiState {
    object Loading : RouterUiState

    object UserAvailable : RouterUiState

    object AuthRequired : RouterUiState
}
