package com.qxlabai.presentation.xmpp.profile

import com.qxlabai.presentation.core.Reducer
import javax.inject.Inject

class ProfileReducer @Inject constructor(

) : Reducer<ProfileState, ProfileAction> {

    override suspend fun reduce(currentState: ProfileState, action: ProfileAction): ProfileState {
        return when (action) {
            ProfileAction.FetchProfile -> currentState.copy(
                isLoading = true,
            )

            is ProfileAction.Profile -> currentState.copy(
                isLoading = false,
                userName = action.user
            )
        }
    }
}