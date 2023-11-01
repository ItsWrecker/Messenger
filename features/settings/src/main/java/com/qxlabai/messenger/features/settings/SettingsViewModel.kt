package com.qxlabai.messenger.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.qxlabai.messenger.core.model.data.DarkConfig
import com.qxlabai.messenger.core.model.data.ThemeBranding
import com.qxlabai.messenger.core.model.data.ThemeConfig
import com.qxlabai.messenger.core.data.repository.PreferencesRepository
import com.qxlabai.messenger.core.model.data.Account
import com.qxlabai.messenger.features.settings.SettingsUiState.Loading
import com.qxlabai.messenger.features.settings.SettingsUiState.Success
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {


    val uiState: StateFlow<SettingsUiState> =
        preferencesRepository.getAccount()
            .map { account ->
                Success(
                    account = account
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = Loading
            )


}

sealed interface SettingsUiState {
    object Loading : SettingsUiState

    data class Success(
        val account: Account
    ) : SettingsUiState
}
