package me.crazystone.minihabits.ui.page

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.crazystone.minihabits.MyApplication
import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.domain.provider.UseCaseProvider
import me.crazystone.minihabits.ui.compose.PopoverCalendar
import me.crazystone.minihabits.ui.theme.ColorTheme
import me.crazystone.minihabits.ui.theme.Dimensions
import me.crazystone.minihabits.ui.viewmodel.TaskViewModel
import me.crazystone.minihabits.ui.viewmodel.TaskViewModelFactory
import me.crazystone.minihabits.utils.Dates
import me.crazystone.minihabits.utils.Logs

class UpdateTodoActivity : ComponentActivity() {
    lateinit var viewModel: TaskViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val task: Task? = intent.getParcelableExtra("task")
        Logs.d("task: " + task)
        viewModel = ViewModelProvider(
            this,
            TaskViewModelFactory(application)
        ).get(TaskViewModel::class.java)
        setContent { UpdateTodoScreen(viewModel, task) }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateTodoScreen(viewModel: TaskViewModel, task: Task?) {
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier
            .background(ColorTheme.backgroundColor),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Update",
                        color = ColorTheme.primaryColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = Dimensions.TEXT_LARGE_SIZE.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { (context as Activity).finish() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
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
                UpdateView(viewModel, task)
            }
        },
    )
}

@Composable
fun UpdateView(viewModel: TaskViewModel, task: Task?) {
    if (task != null) {
        val isChecked = task.isCompleted
        var showCalendar by remember { mutableStateOf(false) }
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked,
                    enabled = false,
                    colors = CheckboxDefaults.colors(
                        checkedColor = ColorTheme.primaryColor400,       // 选中状态时的填充颜色
                        uncheckedColor = ColorTheme.blackColor, // 未选中状态时的填充颜色
                        checkmarkColor = ColorTheme.blackColor,       // 勾选标记的颜色
                        disabledCheckedColor = ColorTheme.backgroundColor         // 禁用状态下的颜色（可选）
                    ),
                    onCheckedChange = {
                    }
                )
                Column {
                    val focusRequester = remember { FocusRequester() }
                    val keyboardController = LocalSoftwareKeyboardController.current
                    var titleText by remember { mutableStateOf(task.title) }
                    val focusManager = LocalFocusManager.current
                    TextField(
                        value = titleText,
                        onValueChange = { newText ->
                            titleText = newText
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .clickable {
                                focusRequester.requestFocus()
                            },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = ColorTheme.transparent,
                            unfocusedContainerColor = ColorTheme.transparent,
                            focusedIndicatorColor = ColorTheme.transparent,
                            unfocusedIndicatorColor = ColorTheme.transparent
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                task.title = titleText
                                viewModel.updateTask(task.copy())
                                focusManager.clearFocus()
                                keyboardController?.hide() // 关闭键盘
                            }
                        ),
                        textStyle = TextStyle(
                            textDecoration = if (isChecked) TextDecoration.LineThrough else null,
                            fontSize = Dimensions.TEXT_BODY_SIZE.sp
                        ),
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text("日期:")
                Spacer(modifier = Modifier.padding(horizontal = 6.dp))
                Text(Dates.getDate(task.scheduledTime), modifier = Modifier.clickable {
                    showCalendar = true
                })
            }
            PopoverCalendar(
                isShow = showCalendar,
                task = task,
                offset = IntOffset(50, 200),
                onDateClick = { dayWeek ->
                    task.scheduledTime = dayWeek.date
                    viewModel.updateTask(task.copy())
                    showCalendar = false
                })
            {
                showCalendar = false
            }
        }
    }
}