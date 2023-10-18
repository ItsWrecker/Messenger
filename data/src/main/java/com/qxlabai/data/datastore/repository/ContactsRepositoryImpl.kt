package com.qxlabai.data.datastore.repository

import com.qxlabai.data.databse.dao.ContactDao
import com.qxlabai.data.databse.entity.ContactEntity
import com.qxlabai.data.databse.entity.asExternalModel
import com.qxlabai.data.datastore.entiry.asEntity
import com.qxlabai.domain.collectors.ContactsRepository
import com.qxlabai.domain.entity.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(
    private val contactDao: ContactDao
) : ContactsRepository{

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