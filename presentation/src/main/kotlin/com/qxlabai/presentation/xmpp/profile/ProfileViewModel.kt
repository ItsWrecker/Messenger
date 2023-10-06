package com.qxlabai.presentation.xmpp.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qxlabai.presentation.core.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val store: Store<ProfileState, ProfileAction>
) : ViewModel(){

    val viewState: StateFlow<ProfileState> = store.state

    fun fetchProfile() = viewModelScope.launch {
        store.dispatch(ProfileAction.FetchProfile)
    }
}