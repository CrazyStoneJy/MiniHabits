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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.crazystone.minihabits.R
import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.ui.compose.CustomModalDialog
import me.crazystone.minihabits.ui.page.HistoryTodoActivity
import me.crazystone.minihabits.ui.page.UpdateTodoActivity
import me.crazystone.minihabits.ui.theme.ColorTheme
import me.crazystone.minihabits.ui.theme.Dimensions
import me.crazystone.minihabits.ui.viewmodel.TaskViewModel
import me.crazystone.minihabits.utils.Dates
import me.crazystone.minihabits.utils.Logs

val History: ImageVector
    get() {
        if (_History != null) {
            return _History!!
        }
        _History = ImageVector.Builder(
            name = "History",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(480f, 840f)
                quadToRelative(-138f, 0f, -240.5f, -91.5f)
                reflectiveQuadTo(122f, 520f)
                horizontalLineToRelative(82f)
                quadToRelative(14f, 104f, 92.5f, 172f)
                reflectiveQuadTo(480f, 760f)
                quadToRelative(117f, 0f, 198.5f, -81.5f)
                reflectiveQuadTo(760f, 480f)
                reflectiveQuadToRelative(-81.5f, -198.5f)
                reflectiveQuadTo(480f, 200f)
                quadToRelative(-69f, 0f, -129f, 32f)
                reflectiveQuadToRelative(-101f, 88f)
                horizontalLineToRelative(110f)
                verticalLineToRelative(80f)
                horizontalLineTo(120f)
                verticalLineToRelative(-240f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(94f)
                quadToRelative(51f, -64f, 124.5f, -99f)
                reflectiveQuadTo(480f, 120f)
                quadToRelative(75f, 0f, 140.5f, 28.5f)
                reflectiveQuadToRelative(114f, 77f)
                reflectiveQuadToRelative(77f, 114f)
                reflectiveQuadTo(840f, 480f)
                reflectiveQuadToRelative(-28.5f, 140.5f)
                reflectiveQuadToRelative(-77f, 114f)
                reflectiveQuadToRelative(-114f, 77f)
                reflectiveQuadTo(480f, 840f)
                moveToRelative(112f, -192f)
                lineTo(440f, 496f)
                verticalLineToRelative(-216f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(184f)
                lineToRelative(128f, 128f)
                close()
            }
        }.build()
        return _History!!
    }

private var _History: ImageVector? = null


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    val context = LocalContext.current
    val tasks by viewModel.tasks.collectAsState()
    var isAIShow by remember { mutableStateOf(false) }
    var isCommonShow by remember { mutableStateOf(false) }
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

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
//                Button(onClick = {
//                    showSheet = true
//                }) {
//                    Text("click")
//                }
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
                            repeat(20) { index ->
                                Text("Item $index", modifier = Modifier.padding(8.dp))
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
                    onAIDismiss()
                }) {
                    Icon(Icons.Filled.Face, contentDescription = "AI")
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                FloatingActionButton(shape = CircleShape, onClick = {
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
    val focusRequesterMap = remember { mutableStateMapOf<Int, FocusRequester>() }
    val focusedItemIndex = remember { mutableStateOf(-1) } // 记录当前获得焦点的索引
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
                    focusRequesterMap = focusRequesterMap,
                    focusedItemIndex = focusedItemIndex
                )
            }
        }
    }
    // 监听当前获得焦点的 TextField
    LaunchedEffect(focusedItemIndex.value) {
        if (focusedItemIndex.value != -1) {
            Logs.d("当前焦点在: ${focusedItemIndex.value}")
        }
    }
}

@Composable
fun TaskItem(
    index: Int,
    task: Task,
    viewModel: TaskViewModel,
    focusRequesterMap: MutableMap<Int, FocusRequester>,
    focusedItemIndex: MutableState<Int>
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
        Column {
            Text(
                modifier = Modifier.clickable {
                    context.startActivity(Intent(context, UpdateTodoActivity::class.java).apply {
                        putExtra("task", task)
                    })
                },
                text = task.title,
                fontSize = Dimensions.TEXT_LARGE_SIZE.sp,
                textDecoration = if (isChecked) TextDecoration.LineThrough else null,
            )
            if (Dates.isAfterToday(task.scheduledTime)) {
                Text(
                    text = Dates.getDate(task.scheduledTime),
                    fontSize = Dimensions.TEXT_SMALL_SIZE.sp,
                    color = ColorTheme.lightGray
                )
            }
        }
    }
}