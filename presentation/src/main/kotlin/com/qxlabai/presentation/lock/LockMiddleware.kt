package com.qxlabai.presentation.lock

import com.qxlabai.domain.events.Events
import com.qxlabai.domain.interactors.lock.SetPassCodeUseCase
import com.qxlabai.domain.interactors.lock.VerifyPassCode
import com.qxlabai.presentation.core.Middleware
import com.qxlabai.presentation.core.Store
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class LockMiddleware @Inject constructor(
    private val setPassCodeUseCase: SetPassCodeUseCase,
    private val verifyPasscodeUseCase: VerifyPassCode
) : Middleware<LockState, LockAction> {

    override suspend fun process(
        action: LockAction,
        currentState: LockState,
        store: Store<LockState, LockAction>
    ) {
        when (action) {
            is LockAction.SetPassCode -> {
                setPassCodeUseCase.invoke(action.passcode)
            }

            is LockAction.VerifyPasscode -> {
                verifyPasscodeUseCase.invoke(params = action.passcode).collectLatest {
                    when (it) {
                        is Events.Error -> store.dispatch(LockAction.OnError(it.message))
                        is Events.Loading -> store.dispatch(LockAction.OnVerifying(it.message))
                        is Events.Success -> store.dispatch(LockAction.OnSuccess(it.data))
                    }
                }
            }

            else -> Unit
        }
    }
}