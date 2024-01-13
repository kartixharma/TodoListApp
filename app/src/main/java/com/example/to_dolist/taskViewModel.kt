package com.example.to_dolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_dolist.data.Item
import com.example.to_dolist.data.ItemDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter

class taskViewModel(private val dao: ItemDao): ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())

    val Listoftasks = dao.getAllTasks().stateIn(viewModelScope, SharingStarted.WhileSubscribed(),
        emptyList())
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val state = combine(_uiState, Listoftasks){ state, tasksList ->
        state.copy(
            tasksList=tasksList
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppUiState())
    fun onEvent(event: ItemEvent){
        when(event){
            ItemEvent.HideDialog -> {
                _uiState.update { it.copy(isAddingTask = false, taskName = "", desc = "", time = "", dateTime = LocalDateTime.of(2050, Month.JANUARY, 12, 2, 18)) }
            }
            ItemEvent.HideEditDialog -> {
                _uiState.update { it.copy(isEditingTask = false, taskName = "", desc = "", time = "", dateTime = LocalDateTime.of(2050, Month.JANUARY, 12, 2, 18)) }
            }
            is ItemEvent.deleteTask -> {
                viewModelScope.launch {
                    dao.delete(event.item)
                }
            }
            ItemEvent.editTask -> {
                val item = Item(
                    id = _uiState.value.editid,
                    taskName = _uiState.value.taskName,
                    isDeleted = false,
                    desc = _uiState.value.desc,
                    time = _uiState.value.time,
                    dateTime = _uiState.value.dateTime.toString()
                )
                viewModelScope.launch(Dispatchers.IO) {
                    dao.editTask(item)
                }
                _uiState.update { it.copy(taskName = "", desc = "", time = "", isEditingTask = false, dateTime = LocalDateTime.of(2050, Month.JANUARY, 12, 2, 18)) }
            }
            ItemEvent.saveTask -> {

                val task = Item(
                    taskName = _uiState.value.taskName,
                    isDeleted = false,
                    desc = _uiState.value.desc,
                    time = _uiState.value.time,
                    dateTime = _uiState.value.dateTime.toString()
                )
                viewModelScope.launch {
                    dao.insert(task)
                }
                _uiState.update { it.copy(taskName = "", desc = "", time = "", isAddingTask = false, dateTime = LocalDateTime.of(2050, Month.JANUARY, 12, 2, 18)) }
            }
            is ItemEvent.setDesc -> {
                _uiState.update { it.copy(desc = event.desc) }
            }
            is ItemEvent.setId -> {
                _uiState.update { it.copy(editid = event.id) }
            }
            is ItemEvent.setTask -> {
                _uiState.update { it.copy(taskName = event.name) }
            }
            ItemEvent.showDialog -> {
                _uiState.update { it.copy(isAddingTask = true) }
            }
            is ItemEvent.showEditDialog -> {
                _uiState.update { it.copy(isEditingTask = true, taskName = event.name, desc = event.desc, time = event.time, dateTime = LocalDateTime.parse(event.dateTime))}
            }

            is ItemEvent.hideTask -> {
                viewModelScope.launch {
                    dao.hideTask(event.id)
                }
            }

            is ItemEvent.showTask -> {
                viewModelScope.launch {
                    dao.showTask(event.id)
                }
            }

            ItemEvent.showTimeDialog -> {
                _uiState.update { it.copy(TimePicking = true) }
            }

            ItemEvent.hideTimeDialog -> {
                _uiState.update { it.copy(TimePicking = false, time = "") }
            }

            is ItemEvent.setTime -> {
                _uiState.update { it.copy(time = event.time, dateTime = event.dateTime, TimePicking = false) }
            }
        }
    }
}