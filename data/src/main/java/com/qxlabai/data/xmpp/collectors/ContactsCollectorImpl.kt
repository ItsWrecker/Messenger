package com.qxlabai.data.xmpp.collectors

import com.qxlabai.domain.collectors.ContactsCollector
import com.qxlabai.domain.collectors.ContactsRepository
import com.qxlabai.domain.entity.Contact
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class ContactsCollectorImpl @Inject constructor(
    private val contactsRepository: ContactsRepository
) : ContactsCollector {
    override suspend fun collectShouldAddToRosterContacts(addToRoster: suspend (List<Contact>) -> Unit) {
        contactsRepository.getShouldAddToRosterStream().collectLatest { contacts ->
            val updatedContacts = contacts.map { it.copy(shouldAddToRoster = false) }
            contactsRepository.updateContacts(updatedContacts)
            addToRoster(updatedContacts)
        }
    }
}