package com.example.to_dolist.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1)
abstract class ItemDatabase: RoomDatabase() {
    abstract val dao: ItemDao
}