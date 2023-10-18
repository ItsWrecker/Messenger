package com.qxlabai.domain.collectors

import com.qxlabai.domain.entity.Contact
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {

    fun getContact(jid: String): Flow<Contact>

    fun getContactsStream(): Flow<List<Contact>>

    fun getShouldAddToRosterStream(): Flow<List<Contact>>

    suspend fun updateContacts(contacts: List<Contact>)
}