package me.crazystone.minihabits.ui.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.ui.theme.ColorTheme
import me.crazystone.minihabits.ui.theme.Dimensions
import me.crazystone.minihabits.ui.viewmodel.TaskViewModel

@SuppressLint("NewApi")
@Composable
fun AddIncompleteTaskView(task: Task, viewModel: TaskViewModel, onClick: () -> Unit) {

//    var showCalendar by remember { mutableStateOf(false) }
    var isRepeat by remember { mutableStateOf(false) }
    var newTask by remember { mutableStateOf(task) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1F)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = false,
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
                                newTask.title = titleText
                                focusManager.clearFocus()
                                keyboardController?.hide() // 关闭键盘
                            }
                        ),
                        textStyle = TextStyle(
                            textDecoration = null,
                            fontSize = Dimensions.TEXT_BODY_SIZE.sp
                        ),
                    )
                }
            }
            RepeatSwitch(isRepeat) { isChecked ->
                isRepeat = isChecked
                newTask.isRepeat = isChecked
            }
//            PopoverCalendar(
//                isShow = showCalendar,
//                task = task,
//                offset = IntOffset(50, 200),
//                onDateClick = { dayWeek ->
//                    newTask.scheduledTime = dayWeek.date
//                    showCalendar = false
//                })
//            {
//                showCalendar = false
//            }
        }
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add", modifier = Modifier
            .padding(8.dp)
            .clickable {
                newTask.scheduledTime = System.currentTimeMillis()
                viewModel.updateTask(newTask.copy())
                onClick()
            })
    }

}