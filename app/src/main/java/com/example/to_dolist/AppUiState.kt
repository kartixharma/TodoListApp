package com.example.to_dolist

import com.example.to_dolist.data.Item
import java.time.LocalDateTime
import java.time.Month

data class AppUiState(
    val tasksList: List<Item> = emptyList(),
    val taskName: String ="",
    val isAddingTask: Boolean = false,
    val isDeleted: Boolean = false,
    val isEditingTask: Boolean = false,
    val index: Int =0,
    val editid: Int =0,
    val desc: String ="",
    val time: String = "",
    val TimePicking: Boolean = false,
    val dateTime: LocalDateTime = LocalDateTime.of(2050, Month.JANUARY, 12, 2, 18)
)
