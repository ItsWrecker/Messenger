package com.qxlabai.messenger.features.lock

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qxlabai.messenger.core.data.repository.LockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LockViewModel @Inject constructor(
    private val lockRepository: LockRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<LockState> = MutableStateFlow(LockState.Idle)
    val uiState: StateFlow<LockState> = _uiState.asStateFlow()

    fun setPasscode(passcode: String) = viewModelScope.launch {
        lockRepository.setPasscode(passcode).let {
            _uiState.update { LockState.PasscodeVerified }
        }
    }

    fun verifyPasscode(passcode: String) = viewModelScope.launch {
        _uiState.update { LockState.Loading }
        val isFirstLogin = lockRepository.isFirstTimeUser()

        return@launch if (isFirstLogin) {
            lockRepository.setPasscode(passcode).let {
                _uiState.update { LockState.PasscodeVerified }
            }
        } else lockRepository.verifyPasscode(passcode).let { status ->
            _uiState.update { if (status) LockState.PasscodeVerified else LockState.InvalidPasscode }
        }
    }

    fun passcodeTyping() = viewModelScope.launch {
        _uiState.update { LockState.PasscodeTyping }
    }
}

sealed interface LockState {
    object Idle : LockState
    object Loading : LockState
    object FirstLogin : LockState
    object PasscodeVerified : LockState

    object InvalidPasscode : LockState

    object PasscodeTyping : LockState

}