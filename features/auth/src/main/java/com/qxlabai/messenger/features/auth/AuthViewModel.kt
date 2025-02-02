package com.qxlabai.messenger.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qxlabai.messenger.core.data.repository.PreferencesRepository
import com.qxlabai.messenger.core.model.data.Account
import com.qxlabai.messenger.core.model.data.AccountStatus.Online
import com.qxlabai.messenger.core.model.data.AccountStatus.RegistrationAlreadyExist
import com.qxlabai.messenger.core.model.data.AccountStatus.RegistrationFailed
import com.qxlabai.messenger.core.model.data.AccountStatus.RegistrationSuccessful
import com.qxlabai.messenger.core.model.data.AccountStatus.ServerNotFound
import com.qxlabai.messenger.core.model.data.AccountStatus.ShouldRegister
import com.qxlabai.messenger.core.model.data.AccountStatus.Unauthorized
import com.qxlabai.messenger.features.auth.AuthUiState.Error
import com.qxlabai.messenger.features.auth.AuthUiState.Idle
import com.qxlabai.messenger.features.auth.AuthUiState.Loading
import com.qxlabai.messenger.features.auth.AuthUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    companion object {
        private const val domain = "wrecker.in"
    }

    private val _uiState: MutableStateFlow<AuthUiState> = MutableStateFlow(Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(jid: String, password: String) {
        val account = Account.create("$jid@$domain", password)
        _uiState.update { Loading }
        viewModelScope.launch {
            preferencesRepository.updateAccount(account)
            checkForAccountStatusChanges()
        }
    }

    fun register(jid: String, password: String) {
        val account = Account.create("$jid@$domain", password, ShouldRegister)
        _uiState.update { Loading }
        viewModelScope.launch {
            preferencesRepository.updateAccount(account)
            checkForAccountStatusChanges()
        }
    }


    private suspend fun checkForAccountStatusChanges() {
        preferencesRepository.getAccount().collect { account ->
            when (account.status) {
                Online -> _uiState.update { Success }
                RegistrationSuccessful -> _uiState.update { Success }
                ServerNotFound -> _uiState.update { Error("Server not available") }
                Unauthorized -> _uiState.update { Error("You are not authorized") }
                RegistrationFailed -> _uiState.update { Error("Registration failed") }
                RegistrationAlreadyExist -> _uiState.update { Error("UUID AlreadyExits") }
                else -> { /*Not interested*/

                }
            }
        }
    }
}


