package com.qxlabai.data.databse

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.qxlabai.data.databse.dao.ContactDao
import com.qxlabai.data.databse.dao.ConversationDao
import com.qxlabai.data.databse.dao.MessageDao
import com.qxlabai.data.databse.dao.SendingChatStateDao
import com.qxlabai.data.databse.entity.ContactEntity
import com.qxlabai.data.databse.entity.ConversationEntity
import com.qxlabai.data.databse.entity.LastMessageEntity
import com.qxlabai.data.databse.entity.MessageEntity
import com.qxlabai.data.databse.entity.SendingChatStateEntity
import com.qxlabai.data.databse.utils.InstantConverter

@Database(
    entities = [ContactEntity::class,
        MessageEntity::class,
        ConversationEntity::class,
        SendingChatStateEntity::class,
        LastMessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    InstantConverter::class
)
abstract class MessengerDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao

    abstract fun messageDao(): MessageDao

    abstract fun conversationDao(): ConversationDao

    abstract fun sendingChatStateDao(): SendingChatStateDao
}