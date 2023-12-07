package com.qxlabai.messenger.features.conversations

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qxlabai.messenger.core.data.repository.ContactsRepository
import com.qxlabai.messenger.core.data.repository.SubscriptionRepository
import com.qxlabai.messenger.core.model.data.Contact
import com.qxlabai.messenger.core.model.data.Subscription
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository,
    private val contactsRepository: ContactsRepository
) : ViewModel() {

    val uiState: StateFlow<SubscriptionUIState> =
        subscriptionRepository.getSubscriptionStream().map {
            SubscriptionUIState.NewSubscription(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SubscriptionUIState.Idle
        )

    fun approve(jid: String) = viewModelScope.launch {
        subscriptionRepository.updateSubscription(Subscription.createApprove(jid))
    }

    fun decline(jid: String) = viewModelScope.launch {
        subscriptionRepository.updateSubscription(
            Subscription.createDecline(jid)
        )
    }

    init {
        viewModelScope.launch {
            subscriptionRepository.isApproved().collect {
                Log.e("SUBSCRIPTION", it.toString())
            }
        }
    }
}

sealed interface SubscriptionUIState {
    object Idle : SubscriptionUIState

    data class NewSubscription(val subscriptions: List<Subscription>) : SubscriptionUIState
}