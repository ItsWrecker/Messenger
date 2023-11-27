package com.qxlabai.messenger.core.data.repository

import com.qxlabai.messenger.core.model.data.Contact
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {

    fun getContact(jid: String): Flow<Contact>

    fun getContactsStream(): Flow<List<Contact>>

    fun getShouldAddToRosterStream(): Flow<List<Contact>>

    suspend fun updateContacts(contacts: List<Contact>)


}
