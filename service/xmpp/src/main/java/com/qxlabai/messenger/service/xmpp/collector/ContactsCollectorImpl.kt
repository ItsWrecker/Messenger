package com.qxlabai.messenger.service.xmpp.collector

import com.qxlabai.messenger.core.model.data.Contact
import com.qxlabai.messenger.core.data.repository.ContactsRepository
import javax.inject.Inject

class ContactsCollectorImpl @Inject constructor(
    private val contactsRepository: ContactsRepository
) : ContactsCollector {

    override suspend fun collectShouldAddToRosterContacts(
        addToRoster: suspend (List<Contact>) -> Unit
    ) {
        contactsRepository.getShouldAddToRosterStream().collect { contacts ->
            val updatedContacts = contacts.map { it.copy(shouldAddToRoster = false) }
            contactsRepository.updateContacts(updatedContacts)
            addToRoster(updatedContacts)
        }
    }
}
