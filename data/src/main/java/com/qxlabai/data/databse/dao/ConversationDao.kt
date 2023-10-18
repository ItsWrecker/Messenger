package com.qxlabai.data.databse.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.qxlabai.data.databse.entity.ConversationEntity
import com.qxlabai.data.databse.entity.PopulatedConversation
import com.qxlabai.domain.entity.ChatState
import kotlinx.coroutines.flow.Flow

/**
 * DAO for [ConversationEntity] access
 * There are multiple update methods to handle various concurrent updates
 * from different parts of application
 */
@Dao
interface ConversationDao {
    @Transaction
    @Query(
        value = """
        SELECT * FROM conversations
        WHERE peer_jid = :peerJid
    """
    )
    fun getConversationEntity(peerJid: String): Flow<PopulatedConversation?>

    /**
     * Get conversations stream
     */
    @Transaction
    @Query(
        value = """
        SELECT * FROM conversations
    """
    )
    fun getConversationEntitiesStream(): Flow<List<PopulatedConversation>>

    /**
     * Inserts [conversationEntity] into the db if it doesn't exist, and ignores if it exists
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(conversationEntity: ConversationEntity): Long

    @Query(
        """
            UPDATE conversations
            SET
            unread_messages_count = :unreadMessagesCount,
            chat_state = :chatState,
            last_message_id = :lastMessageId
            WHERE peer_jid = :peerJid
        """
    )
    suspend fun update(
        peerJid: String,
        unreadMessagesCount: Int,
        chatState: ChatState,
        lastMessageId: Long
    )

    @Query(
        """
            UPDATE conversations
            SET 
            unread_messages_count = :unreadMessagesCount,
            is_chat_open = :isChatOpen
            WHERE peer_jid = :peerJid
        """
    )
    suspend fun update(
        peerJid: String,
        unreadMessagesCount: Int,
        isChatOpen: Boolean
    )

    @Query(
        """
            UPDATE conversations
            SET last_message_id = :lastMessageId
            WHERE peer_jid = :peerJid
        """
    )
    suspend fun update(
        peerJid: String,
        lastMessageId: Long
    )

    @Query(
        """
            UPDATE conversations
            SET chat_state = :chatState
            WHERE peer_jid = :peerJid
        """
    )
    suspend fun update(
        peerJid: String,
        chatState: ChatState
    )

    @Query(
        """
            UPDATE conversations
            SET draft_message = :draftMessage
            WHERE peer_jid = :peerJid
        """
    )
    suspend fun update(
        peerJid: String,
        draftMessage: String?
    )

    @Query(
        """
            UPDATE conversations
            SET is_chat_open = :isChatOpen
            WHERE peer_jid = :peerJid
        """
    )
    suspend fun update(
        peerJid: String,
        isChatOpen: Boolean
    )
}