package com.lvsmsmch.lchat.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lvsmsmch.lchat.data.db.dao.MessagesDao
import com.lvsmsmch.lchat.data.db.entities.ChatMessageEntity

const val DATABASE_NAME = "room_db"
private const val CURRENT_DB_VERSION = 1

@Database(
    entities = [
        ChatMessageEntity::class,
    ],
    version = CURRENT_DB_VERSION
)
abstract class AppDB : RoomDatabase() {
    abstract fun messagesDao(): MessagesDao
}