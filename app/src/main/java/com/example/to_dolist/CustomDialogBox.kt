package com.example.to_dolist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun CustomDialogBox(state: AppUiState, onEvent: (ItemEvent) ->Unit){
    Dialog(onDismissRequest = { onEvent(ItemEvent.HideDialog) },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.fillMaxWidth(0.95f),
            border = BorderStroke(1.dp, Color.Gray),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            )
        ) {
            Column( modifier = Modifier
                .padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(25.dp)) {
                Text(
                    text = "Enter New Task",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                OutlinedTextField(label = { Text(text = "Enter task here" )}, modifier = Modifier.align(Alignment.CenterHorizontally),
                    value = state.taskName,
                    onValueChange ={onEvent(ItemEvent.setTask(it))},
                    shape = RoundedCornerShape(20.dp))
                OutlinedTextField(label = { Text(text = "Enter description here" )}, modifier = Modifier.align(Alignment.CenterHorizontally),
                    value = state.desc,
                    onValueChange ={onEvent(ItemEvent.setDesc(it))},
                    shape = RoundedCornerShape(20.dp) )
                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    TextButton(onClick = { onEvent(ItemEvent.showTimeDialog)}) {
                        Text(text = "Set Reminder", style = MaterialTheme.typography.titleLarge)
                    }
                    Icon(Icons.Filled.Notifications, contentDescription = null ,modifier = Modifier.align(Alignment.CenterVertically))
                }
                Row {
                    TextButton(onClick = { onEvent(ItemEvent.saveTask)}) {
                        Text(text = "Save", style = MaterialTheme.typography.titleLarge, color = Color(0xFF09a129))
                    }
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = { onEvent(ItemEvent.HideDialog)}) {
                        Text(text = "Cancel", style = MaterialTheme.typography.titleLarge, color= MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}