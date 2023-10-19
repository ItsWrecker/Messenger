package com.qxlabai.messenger.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.qxlabai.messenger.core.model.data.DarkConfig
import com.qxlabai.messenger.core.model.data.ThemeBranding
import com.qxlabai.messenger.core.model.data.ThemeConfig
import com.qxlabai.messenger.core.data.repository.PreferencesRepository
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
        preferencesRepository.getThemeConfig()
            .map { themeConfig ->
                Success(
                    themeConfig = themeConfig,
                    themeBrandings = ThemeBranding.values().toList(),
                    darkConfigs = DarkConfig.values().toList()
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = Loading
            )

    fun selectThemeBranding(themeBranding: ThemeBranding) {
        if (uiState.value is Success) {
            val themeConfig = (uiState.value as Success).themeConfig

            viewModelScope.launch {
                preferencesRepository.updateThemeConfig(
                    themeConfig.copy(themeBranding = themeBranding)
                )
            }
        }
    }

    fun selectDarkConfig(darkConfig: DarkConfig) {
        if (uiState.value is Success) {
            val themeConfig = (uiState.value as Success).themeConfig

            viewModelScope.launch {
                preferencesRepository.updateThemeConfig(
                    themeConfig.copy(darkConfig = darkConfig)
                )
            }
        }
    }
}

sealed interface SettingsUiState {
    object Loading : SettingsUiState

    data class Success(
        val themeConfig: ThemeConfig,
        val themeBrandings: List<ThemeBranding>,
        val darkConfigs: List<DarkConfig>
    ) : SettingsUiState
}
