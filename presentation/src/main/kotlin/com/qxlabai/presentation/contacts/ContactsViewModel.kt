package com.qxlabai.presentation.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qxlabai.domain.collectors.ContactsRepository
import com.qxlabai.domain.entity.Contact
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContactsViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository
) : ViewModel() {

    val uiState: StateFlow<ContactsUiState> = contactsRepository.getContactsStream()
        .map { contacts ->
            ContactsUiState.Success(contacts)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ContactsUiState.Loading
        )


    fun addContact(contact: String) {
        viewModelScope.launch {
            contactsRepository.updateContacts(
                listOf(
                    Contact.create(jid = contact)
                )
            )
        }
    }
}