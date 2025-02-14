package me.crazystone.minihabits.ui


import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.crazystone.minihabits.R
import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.ui.compose.AddIncompleteTaskView
import me.crazystone.minihabits.ui.compose.CustomModalDialog
import me.crazystone.minihabits.ui.page.HistoryTodoActivity
import me.crazystone.minihabits.ui.page.UpdateTodoActivity
import me.crazystone.minihabits.ui.theme.ColorTheme
import me.crazystone.minihabits.ui.theme.Dimensions
import me.crazystone.minihabits.ui.viewmodel.TaskViewModel
import me.crazystone.minihabits.utils.Dates
import me.crazystone.minihabits.utils.Logs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    val context = LocalContext.current
    val tasks by viewModel.tasks.collectAsState()
    val incompleteTasks by viewModel.incompleteTasks.collectAsState()
    Logs.d("incompleteTasks size: " + incompleteTasks.size)
    var isAIShow by remember { mutableStateOf(false) }
    var isCommonShow by remember { mutableStateOf(false) }
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var isFirstShowSheet by remember { mutableStateOf(false) }

    LaunchedEffect(incompleteTasks) {
        if (incompleteTasks.isNotEmpty() && !isFirstShowSheet) {
            isFirstShowSheet = true
            showSheet = true
        }
    }

    Scaffold(
        modifier = Modifier
            .background(ColorTheme.backgroundColor),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Todo",
                        color = ColorTheme.primaryColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = Dimensions.TEXT_LARGE_SIZE.sp
                    )
                },
                actions = {
                    IconButton(onClick = {
                        val intent = Intent(context, HistoryTodoActivity::class.java)
                        context.startActivity(intent)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_history),
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

                if (showSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showSheet = false }, // 点击外部关闭
                        sheetState = sheetState
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            incompleteTasks.forEachIndexed { index, task ->
//                                Text("Item $index", modifier = Modifier.padding(8.dp))
                                AddIncompleteTaskView(task, viewModel) {
                                    Logs.d("click Add AddIncompleteTaskView")
                                    showSheet = false
                                }
                            }
                        }
                    }
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
                    isExpanded = false
                    onAIDismiss()
                }) {
                    Icon(Icons.Filled.Face, contentDescription = "AI")
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                FloatingActionButton(shape = CircleShape, onClick = {
                    isExpanded = false
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
fun TaskList(tasks: List<Task>, viewModel: TaskViewModel) {
    if (tasks.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No tasks available", style = MaterialTheme.typography.bodyMedium)
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(tasks) { index, task ->
                TaskItem(
                    index = index,
                    task = task,
                    viewModel = viewModel,
                )
            }
        }
    }
}

@Composable
fun TaskItem(
    index: Int,
    task: Task,
    viewModel: TaskViewModel,
) {
    val isChecked = task.isCompleted
    val context = LocalContext.current
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
                Logs.d("index: $index")
                viewModel.updateTask(
                    task.copy(
                        isCompleted = !isChecked,
                        updateTime = System.currentTimeMillis()
                    )
                )
            }
        )
        Column(modifier = Modifier.clickable {
            context.startActivity(Intent(context, UpdateTodoActivity::class.java).apply {
                putExtra("task", task)
            })
        }) {
            Text(
                text = task.title,
                fontSize = Dimensions.TEXT_LARGE_SIZE.sp,
                textDecoration = if (isChecked) TextDecoration.LineThrough else null,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (Dates.isAfterToday(task.scheduledTime)) {
                    Text(
                        text = Dates.getDate(task.scheduledTime),
                        fontSize = Dimensions.TEXT_SMALL_SIZE.sp,
                        color = ColorTheme.lightGray
                    )
                }
                if (task.isRepeat) {
                    Icon(
                        painter = painterResource(R.drawable.icon_repeat),
                        tint = ColorTheme.primaryColor,
                        contentDescription = "repeat",
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                    )
                }
            }
        }
    }
}