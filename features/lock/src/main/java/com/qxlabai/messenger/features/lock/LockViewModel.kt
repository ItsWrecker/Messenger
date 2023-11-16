package com.qxlabai.messenger.features.lock

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qxlabai.messenger.core.data.repository.EraseRepository
import com.qxlabai.messenger.core.data.repository.LockRepository
import com.qxlabai.messenger.core.data.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LockViewModel @Inject constructor(
    private val lockRepository: LockRepository,
    private val eraseRepository: EraseRepository,
    private val preferencesRepository: PreferencesRepository,
    @ApplicationContext private val context: Context
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
            _uiState.update {
                if (status) {
                    eraseRepository.updateAttempts(true)
                    LockState.PasscodeVerified
                } else {
                    eraseRepository.updateAttempts(false)
                    val attempts = eraseRepository.attempts()
                    return@update if (attempts == 2) {
                        LockState.AttemptsWarming
                    } else if (attempts > 2) {
                        LockState.Reset
                    } else LockState.InvalidPasscode
                }
            }
        }
    }

    fun passcodeTyping() = viewModelScope.launch {
        _uiState.update { LockState.PasscodeTyping }
    }

    fun eraseEverything() = viewModelScope.launch {
        clearAppData()
        _uiState.update { LockState.OnErased }
    }

    private fun clearAppData() {
        try {
            Runtime.getRuntime().exec("pm clear " + context.packageName)
            val broadcastIntent = Intent("com.qxlabai.messenger.ACTION_APP_DATA_CLEARED")
            context.sendBroadcast(broadcastIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

sealed interface LockState {
    object Idle : LockState
    object Loading : LockState
    object FirstLogin : LockState
    object PasscodeVerified : LockState

    object InvalidPasscode : LockState

    object PasscodeTyping : LockState

    object Reset : LockState

    object AttemptsWarming : LockState

    object OnErased : LockState

}