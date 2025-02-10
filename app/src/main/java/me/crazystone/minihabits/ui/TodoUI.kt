package me.crazystone.minihabits.ui

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.ui.compose.ClickableText
import me.crazystone.minihabits.ui.compose.CustomModalDialog
import me.crazystone.minihabits.ui.page.HistoryTodoActivity
import me.crazystone.minihabits.ui.theme.ColorTheme
import me.crazystone.minihabits.ui.theme.Dimensions
import me.crazystone.minihabits.ui.viewmodel.TaskViewModel
import me.crazystone.minihabits.utils.Dates


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    val context = LocalContext.current
    val tasks by viewModel.tasks.collectAsState()
    var isAIShow by remember { mutableStateOf(false) }
    var isCommonShow by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .background(ColorTheme.backgroundColor),
        topBar = {
            TopAppBar(
                title = { Text("Todo", color = ColorTheme.primaryColor, fontWeight = FontWeight.Bold, fontSize = Dimensions.TEXT_LARGE_SIZE.sp) },
                actions = {
                    IconButton(onClick = {
                        val intent = Intent(context, HistoryTodoActivity::class.java)
                        context.startActivity(intent)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Localized description"
                        )
                    }
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = Dimensions.HORIZONTAL_MARGIN.dp),
            ) {
                TaskList(tasks, viewModel)
                CustomModalDialog(isCommonShow, { task ->
                    viewModel.addTask(task)
                }) {
                    isCommonShow = false
                }
            }
        },
        floatingActionButton = {
            FloatingButton({

            }) {
                isCommonShow = true
            }
        },
        floatingActionButtonPosition = FabPosition.End
    )
}

@Composable
fun FloatingButton(onAIDismiss: () -> Unit, onCommonDismiss: () -> Unit) {
    var isExpanded by remember { mutableStateOf(false) } // 是否展开子菜单
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 45f else 0f, // 0° ↔ 90° 切换
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
    )

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(16.dp), // 子 FAB 之间的间距
            modifier = Modifier.padding(16.dp)
        ) {
            AnimatedVisibility(visible = isExpanded) {
                FloatingActionButton(shape = CircleShape, onClick = {
                    onAIDismiss()
                }) {
                    Icon(Icons.Filled.Face, contentDescription = "AI")
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                FloatingActionButton(shape = CircleShape,onClick = {
                    onCommonDismiss()
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            }

            FloatingActionButton(
                onClick = {
                    isExpanded = !isExpanded
                },
                contentColor = ColorTheme.blackColor,
                containerColor = if (isExpanded) ColorTheme.primaryColor400 else ColorTheme.primaryColor,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp),
                modifier = Modifier.graphicsLayer(rotationZ = rotationAngle)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add",

                )
            }
        }
    }
}

@Composable
fun TaskInput(viewModel: TaskViewModel) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                viewModel.addTask(title, description)
                title = ""
                description = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Task")
        }
    }
}

@Composable
fun TaskList(tasks: List<Task>, viewModel: TaskViewModel) {
    if (tasks.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No tasks available", style = MaterialTheme.typography.bodyMedium)
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(tasks) { task ->
                TaskItem(task = task, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, viewModel: TaskViewModel) {
    val isChecked = task.isCompleted

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Checkbox(
            checked = isChecked,
            colors = CheckboxDefaults.colors(
                checkedColor = ColorTheme.primaryColor400,       // 选中状态时的填充颜色
                uncheckedColor = ColorTheme.blackColor, // 未选中状态时的填充颜色
                checkmarkColor = ColorTheme.blackColor,       // 勾选标记的颜色
            ),
            onCheckedChange = { checked ->
                viewModel.updateTask(task.copy(isCompleted = !isChecked, updateTime = System.currentTimeMillis()))
            }
        )
        Column {
            Text(
                text = task.title,
                textDecoration = if (isChecked) TextDecoration.LineThrough else null,
            )
            if(Dates.isAfterToday(task.scheduledTime)) {
                Text(
                    text =  Dates.getDate(task.scheduledTime),
                    fontSize = Dimensions.TEXT_SMALL_SIZE.sp,
                    color = ColorTheme.warningColor
                )
            }
        }
    }
}