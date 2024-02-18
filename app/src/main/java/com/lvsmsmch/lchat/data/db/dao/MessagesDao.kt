package com.lvsmsmch.lchat.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lvsmsmch.lchat.data.db.entities.ChatMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessagesDao {

    @Query("SELECT * FROM chat_messages WHERE characterId = :characterId AND id = :messageId")
    fun getMessage(characterId: Int, messageId: Int): ChatMessageEntity

    @Query("SELECT * FROM chat_messages WHERE characterId = :characterId")
    fun getMessages(characterId: Int): List<ChatMessageEntity>

    @Query("SELECT * FROM chat_messages WHERE characterId = :characterId")
    fun getMessagesFlow(characterId: Int): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(chatMessageEntity: ChatMessageEntity)

    @Query("DELETE FROM chat_messages WHERE characterId = :characterId")
    fun deleteAll(characterId: Int)

    @Query("DELETE FROM chat_messages WHERE characterId = :characterId AND id = :messageId")
    fun deleteById(characterId: Int, messageId: Int)
}