package com.example.to_dolist

import com.example.to_dolist.data.Item
import java.time.LocalDateTime

sealed interface ItemEvent {
    object saveTask: ItemEvent
    data class setTask(val name: String): ItemEvent
    data class setDesc(val desc: String): ItemEvent
    data class setId(val id: Int): ItemEvent
    object editTask: ItemEvent
    data class hideTask(val id: Int): ItemEvent
    data class showTask(val id: Int): ItemEvent
    object showDialog: ItemEvent
    object HideDialog: ItemEvent
    data class showEditDialog(
        val desc: String,
        val name: String,
        val time: String,
        val dateTime: String
    ): ItemEvent
    object HideEditDialog: ItemEvent
    object showTimeDialog: ItemEvent
    object hideTimeDialog: ItemEvent
    data class setTime(val time: String, val dateTime: LocalDateTime): ItemEvent
    data class deleteTask(val item: Item): ItemEvent
}