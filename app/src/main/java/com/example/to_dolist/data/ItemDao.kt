package com.example.to_dolist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("select * from items where isDeleted=0")
    fun getAllTasks(): Flow<List<Item>>

    @Update
    suspend fun editTask(item: Item)

    @Query("update items set isDeleted=1 where id=:id")
    suspend fun hideTask(id: Int)

    @Query("update items set isDeleted=0 where id=:id")
    suspend fun showTask(id: Int)
}