package com.qxlabai.presentation.lock

import com.qxlabai.presentation.core.Action

sealed interface LockAction : Action {
    data class SetPassCode(val passcode: String): LockAction
    data class VerifyPasscode(val passcode: String): LockAction

    data class OnSuccess(val verified: Boolean): LockAction
    data class OnVerifying(val message: String): LockAction
    data class OnError(val message: String): LockAction
}