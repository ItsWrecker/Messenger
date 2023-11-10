package com.qxlabai.messenger.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.qxlabai.messenger.core.database.dao.ContactDao
import com.qxlabai.messenger.core.database.dao.ConversationDao
import com.qxlabai.messenger.core.database.dao.MessageDao
import com.qxlabai.messenger.core.database.dao.SendingChatStateDao
import com.qxlabai.messenger.core.database.model.ContactEntity
import com.qxlabai.messenger.core.database.model.ConversationEntity
import com.qxlabai.messenger.core.database.model.LastMessageEntity
import com.qxlabai.messenger.core.database.model.MessageEntity
import com.qxlabai.messenger.core.database.model.SendingChatStateEntity
import com.qxlabai.messenger.core.database.util.InstantConverter

@Database(
    entities = [
        ContactEntity::class,
        MessageEntity::class,
        ConversationEntity::class,
        SendingChatStateEntity::class,
        LastMessageEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    InstantConverter::class,
)
abstract class MessengerDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao

    abstract fun messageDao(): MessageDao

    abstract fun conversationDao(): ConversationDao

    abstract fun sendingChatStateDao(): SendingChatStateDao


}
