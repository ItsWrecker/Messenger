package com.qxlabai.presentation.lock

import com.qxlabai.presentation.core.Reducer
import javax.inject.Inject

class LockReducer @Inject constructor() : Reducer<LockState, LockAction> {
    override suspend fun reduce(currentState: LockState, action: LockAction): LockState {
        return when (action) {
            is LockAction.OnError -> currentState.copy(
                isVerified = false,
                error = action.message,
            )

            is LockAction.OnSuccess -> currentState.copy(
                isVerified = action.verified,
                error = null,
            )

            is LockAction.OnVerifying -> currentState.copy(
                isVerified = false,
                error = null,
                status = "Verifying"
            )

            else -> currentState
        }
    }
}