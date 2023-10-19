package com.qxlabai.messenger.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.qxlabai.messenger.core.database.model.SendingChatStateEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for [SendingChatStateEntity] access
 */
@Dao
interface SendingChatStateDao {

    @Query(value = "SELECT * FROM sending_chat_state where consumed = 0")
    fun getSendingChatStateEntitiesStream(): Flow<List<SendingChatStateEntity>>

    /**
     * Inserts [sendingChatStateEntity] into the db if it doesn't exist, and update if it do
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(sendingChatStateEntity: SendingChatStateEntity)
}
