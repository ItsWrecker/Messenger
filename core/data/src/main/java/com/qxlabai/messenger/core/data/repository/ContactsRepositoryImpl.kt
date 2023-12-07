package com.qxlabai.messenger.core.data.repository

import com.qxlabai.messenger.core.model.data.Contact
import com.qxlabai.messenger.core.data.model.asEntity
import com.qxlabai.messenger.core.database.dao.ContactDao
import com.qxlabai.messenger.core.database.model.ContactEntity
import com.qxlabai.messenger.core.database.model.asExternalModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class ContactsRepositoryImpl @Inject constructor(
    private val contactDao: ContactDao
) : ContactsRepository {

    override fun getContact(jid: String): Flow<Contact> =
        contactDao.getContactEntity(jid).map(ContactEntity::asExternalModel)

    override fun getContactsStream(): Flow<List<Contact>> =
        contactDao.getContactEntitiesStream()
            .map { it.map(ContactEntity::asExternalModel) }

    override fun getShouldAddToRosterStream(): Flow<List<Contact>> =
        contactDao.getShouldAddToRosterStream()
            .map { it.map(ContactEntity::asExternalModel) }

    override suspend fun updateContacts(contacts: List<Contact>) =
        contactDao.upsert(contacts.map(Contact::asEntity))


}
