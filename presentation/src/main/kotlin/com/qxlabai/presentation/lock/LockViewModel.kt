package com.qxlabai.presentation.lock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qxlabai.presentation.core.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LockViewModel @Inject constructor(
    private val store: Store<LockState, LockAction>
) : ViewModel() {

    val viewState: StateFlow<LockState> = store.state

    fun processAction(action: LockAction) = viewModelScope.launch {
        when (action) {
            is LockAction.SetPassCode -> {
                if (action.passcode.isNotEmpty()) store.dispatch(LockAction.SetPassCode(action.passcode))
            }

            is LockAction.VerifyPasscode -> {
                if (action.passcode.isNotEmpty()) store.dispatch(LockAction.VerifyPasscode(action.passcode))
            }

            else -> Unit
        }

    }

}