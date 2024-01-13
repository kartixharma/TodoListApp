package com.example.to_dolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.StringBufferInputStream
import java.time.LocalDateTime

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val taskName: String,
    val isDeleted: Boolean,
    val desc: String,
    val time: String,
    val dateTime: String
)
