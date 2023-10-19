package com.qxlabai.messenger.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.qxlabai.messenger.core.database.model.ContactEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for [ContactEntity] access
 */
@Dao
interface ContactDao {
    @Query(
        value = """
        SELECT * FROM contacts
        WHERE jid = :jid
    """
    )
    fun getContactEntity(jid: String): Flow<ContactEntity>

    @Query(value = "SELECT * FROM contacts")
    fun getContactEntitiesStream(): Flow<List<ContactEntity>>

    @Query(
        value = """
        SELECT * FROM contacts
        WHERE should_add_to_roster = 1
    """
    )
    fun getShouldAddToRosterStream(): Flow<List<ContactEntity>>

    /**
     * Inserts [contactEntities] into the db if they don't exist, and update those that do
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(contactEntities: List<ContactEntity>)
}
