@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.to_dolist

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDismissState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.to_dolist.data.Item
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Locale

@Composable
fun TasksScreen(
    state: AppUiState,
    onEvent: (ItemEvent) -> Unit,
    onClick: (LocalDateTime, String) -> Unit,
    onCancel: (LocalDateTime, String) -> Unit
) {
    val notLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()){

    }
    LaunchedEffect(key1 = true){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
    val formatter = remember {
        SimpleDateFormat(("hh:mm a"), Locale.getDefault())
    }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {onEvent(ItemEvent.showDialog)},
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.size(70.dp),
                containerColor = colorScheme.inversePrimary
            ){
                    Image(painter = painterResource(id = R.drawable.back), contentDescription = null, modifier = Modifier
                        .scale(3f).rotate(180f)
                        .fillMaxSize())
                    Icon(Icons.Filled.Add, contentDescription = "ADD", Modifier.size(35.dp), tint = Color.White)
            }
        }

    ) { innerpadding ->
        Image(painter = painterResource(id = R.drawable.back), contentDescription = null, modifier = Modifier
            .scale(1.3f)
            .fillMaxSize())
        Column(modifier = Modifier.fillMaxWidth().padding(top = 65.dp)) {
            Text(text = "Todo List",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 35.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFF9EEFE))
        }
        AnimatedVisibility(state.isAddingTask) {
            CustomDialogBox(
                state = state,
                onEvent = onEvent
            )
        }
        AnimatedVisibility(state.isEditingTask){
            EditDialogBox(
                onClick={time, name ->
                        onClick(time, name)
                },
                state = state,
                onEvent = onEvent
            )
        }
        AnimatedVisibility(
            state.tasksList.isEmpty(),
            enter = slideInHorizontally(), exit = slideOutHorizontally()){
            Column {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 575.dp)
                        .height(100.dp)
                ) {
                    Text(
                        text = "No Tasks!",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = typography.displayMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Text(
                        text = "Click + to add a task",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = typography.headlineMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.pngegg), contentDescription = null,
                    Modifier
                        .padding(start = 160.dp)
                        .size(150.dp)
                        .rotate(degrees = -90.5F)
                )
            }
        }
        val cal = Calendar.getInstance()
        val Tstate = rememberTimePickerState(is24Hour = false)
        AnimatedVisibility(state.TimePicking) {
            TimePickerDialog(onCancel={onEvent(ItemEvent.hideTimeDialog)},
                onConfirm = {
                    cal.set(Calendar.HOUR_OF_DAY, Tstate.hour)
                    cal.set(Calendar.MINUTE, Tstate.minute)
                    cal.isLenient=false
                    onEvent(ItemEvent.setTime(formatter.format(cal.time), cal.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()))
                    onClick(cal.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), state.taskName)
                }) {
                TimePicker(state = Tstate)
            }
        }
        LazyColumn(state = rememberLazyListState(),
            modifier = Modifier
                .padding(top = 140.dp)
        ) {
            items(items = state.tasksList, key = { task -> task.id}) {task->

                val stat = rememberDismissState(confirmValueChange = {
                    if (it == DismissValue.DismissedToStart){
                        onEvent(ItemEvent.hideTask(task.id)).also {
                        scope.launch {
                            val result = snackbarHostState
                                .showSnackbar(
                                    message = "Task Deleted!",
                                    actionLabel = "Undo",
                                    duration = SnackbarDuration.Short
                                )
                            when (result) {
                                SnackbarResult.ActionPerformed -> {
                                    onEvent(ItemEvent.showTask(task.id))
                                }
                                SnackbarResult.Dismissed -> {
                                    onEvent(ItemEvent.deleteTask(task))
                                    onCancel(LocalDateTime.parse(task.dateTime), task.taskName)
                                }
                            }
                        }
                    }
                        true
                    }
                    else{
                        false
                    }
                })
                SwipeToDismiss(state = stat, background = {
                    val color by animateColorAsState(
                        when (stat.targetValue) {
                            DismissValue.DismissedToStart -> Color(0xFFFA8072)
                            else -> Color.Transparent
                        }, label = ""
                    )
                    val iconScale by animateFloatAsState(
                        targetValue = if (stat.targetValue == DismissValue.DismissedToStart) 1.3f else 0.8f,
                        label = ""
                    )
                    Box(modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, bottom = 7.dp)
                        .fillMaxSize()
                        .background(color, shape = RoundedCornerShape(15.dp))){
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete",
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 20.dp)
                                .scale(iconScale))
                    }
                }, dismissContent = {
                        TaskItem(onEvent = onEvent,
                            task = task)
                },directions = setOf(DismissDirection.EndToStart))
            }
        }
    }
}
@Composable
fun TaskItem(onEvent: (ItemEvent) -> Unit,
              task: Item
){
    var checked by rememberSaveable {
        mutableStateOf(false)
    }
    Card(modifier = Modifier
        .padding(start = 13.dp, end = 13.dp, bottom = 7.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        shape = RoundedCornerShape(15.dp)
        ) {
        Row(

        ) {
            Column {
                Text(
                    text = task.taskName, modifier = Modifier
                        .padding(30.dp, top = 10.dp),
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = if(checked) TextDecoration.LineThrough else null,
                    style = typography.headlineSmall,
                    maxLines = 1
                )
                Text(
                    text = task.desc, modifier = Modifier
                        .padding(30.dp, top = 3.dp)
                        .width(210.dp),
                    style = typography.titleLarge,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = { checked=!checked },
                modifier = Modifier
                    .padding(top =15.dp)
            ) {
                Icon(
                    if (!checked) painterResource(id =R.drawable.pngfind_com_checkmark_png_1827925) else painterResource(id =R.drawable.filledtick),
                    contentDescription = "Edit",
                    Modifier.scale(0.6f), tint = MaterialTheme.colorScheme.primary
                    )
            }
            Column {
                IconButton(
                    onClick = { onEvent(ItemEvent.showEditDialog(task.desc, task.taskName, task.time, task.dateTime)).also { onEvent(ItemEvent.setId(task.id)) }},
                    modifier = Modifier
                        .padding(end=15.dp, top = 15.dp, start=15.dp)
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit")
                }
                Text(
                    text = task.time, modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 5.dp),
                    style = typography.labelLarge
                )
            }
        }
    }
}
