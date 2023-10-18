package com.qxlabai.presentation.router


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qxlabai.domain.repositories.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RouterViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {


    val uiState: StateFlow<RouterUiState> = preferencesRepository.getAccount()
        .map { account ->

            if (account.alreadyLoggedIn) {
                RouterUiState.UserAvailable
            } else {
                RouterUiState.AuthRequired
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RouterUiState.Loading
        )


}