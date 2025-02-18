package me.crazystone.minihabits.ui.compose

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.ui.theme.ColorTheme
import me.crazystone.minihabits.ui.theme.Dimensions
import me.crazystone.minihabits.utils.Dates
import me.crazystone.minihabits.utils.Logs

@Composable
fun CustomModalDialog(
    showDialog: Boolean,
    onAddSuccess: (task: Task) -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(dismissOnClickOutside = false) // 点击外部不关闭
        ) {
            var newTask: Task? by remember { mutableStateOf(null) }
            var title by remember { mutableStateOf("") }
            var showCalendar by remember { mutableStateOf(false) }
            var isRepeat by remember { mutableStateOf(false) }

            val context = LocalContext.current

            Box(
                modifier = Modifier
                    .background(
                        ColorTheme.whiteColor,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Add Todo",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ColorTheme.blackColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = Dimensions.TEXT_MID_SIZE.sp
                        )
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "close",
                            modifier = Modifier.clickable {
                                onDismiss()
                            })
                    }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        MHTextField(
                            value = title,
                            placeholder = "主人你要做什么...",
                            modifier = Modifier
                                .border(0.dp, Color.Transparent)
                                .weight(1F)
                        ) {
                            title = it
                        }
                        MHButton("Add") {
                            if (title != "") {
                                if (newTask == null) {
                                    newTask = Task(
                                        title = title,
                                        description = title,
                                        isRepeat = false,
                                        repeatType = 0
                                    )
                                }
                                newTask?.title = title
                                Logs.d("task now: $newTask")
                                onAddSuccess(newTask?.copy()!!)
                                onDismiss()
                            } else {
                                Toast.makeText(context, "请输入title", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "calendar",
                            modifier = Modifier
                                .padding(4.dp)
                                .clickable {
                                    showCalendar = true
                                })
                        (if (newTask === null || newTask?.scheduledTime?.let { Dates.isToday(it) } == true) "今天" else newTask?.scheduledTime?.let {
                            Dates.getDate(
                                it
                            )
                        })?.let { Text(text = it) }
                    }
                    RepeatSwitch(isRepeat) { isChecked ->
                        isRepeat = isChecked
                        if (newTask == null) {
                            newTask =
                                Task(title = "", description = "", isRepeat = false, repeatType = 0)
                        }
                        newTask?.isRepeat = isChecked
                        newTask?.repeatType = if (isChecked) 1 else 0
                    }
                    PopoverCalendar(
                        isShow = showCalendar,
                        task = newTask,
                        offset = null,
                        onDateClick = { dayWeek ->
                            if (newTask == null) {
                                newTask = Task(
                                    title = "",
                                    description = "",
                                    isRepeat = false,
                                    repeatType = 0
                                )
                            }
                            newTask?.scheduledTime = dayWeek.date
                            showCalendar = false
                        }) {
                        showCalendar = false
                    }
                }
            }
        }
    }
}