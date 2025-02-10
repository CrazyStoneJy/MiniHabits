package me.crazystone.minihabits.ui.compose

import android.app.Activity
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
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
            val configuration = LocalConfiguration.current
            val screenWidthDp = configuration.screenWidthDp.dp  // 屏幕宽度（dp）
            val screenHeightDp = configuration.screenHeightDp.dp // 屏幕高度（dp）

            val density = LocalDensity.current
            val screenWidth = with(density) { screenWidthDp.toPx().toInt() }   // 转换为像素（px）
            val screenHeight = with(density) { screenHeightDp.toPx().toInt() } // 转换为像素（px）

            val context = LocalContext.current

            Box(
                modifier = Modifier
//                    .fillMaxWidth(0.9f) // 设置弹窗宽度
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
                    Text(
                        "主人你要做什么...",
                        color = ColorTheme.primaryColor,
                        fontSize = Dimensions.TEXT_MID_SIZE.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextField(
                            value = title,
                            onValueChange = { title = it },
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent, // 去掉聚焦时的下划线
                                unfocusedIndicatorColor = Color.Transparent, // 去掉未聚焦时的下划线
                                disabledIndicatorColor = Color.Transparent // 去掉禁用时的下划线
                            )
                        )
                        MHButton("Add") {
                            if (title != "") {
                                if (newTask == null) {
                                    newTask = Task(title = title, description = title)
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
                    Row(horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "calendar",
                            modifier = Modifier.padding(4.dp).clickable {
                                showCalendar = true
                            })
                        (if (newTask === null || newTask?.scheduledTime?.let { Dates.isToday(it) } == true) "今天" else newTask?.scheduledTime?.let {
                            Dates.getDate(
                                it
                            )
                        })?.let { Text(text = it) }
                    }

                    Box{
                        if (showCalendar) {
                            Popup(
                                popupPositionProvider = object : PopupPositionProvider {
                                    override fun calculatePosition(
                                        anchorBounds: IntRect,
                                        windowSize: IntSize,
                                        layoutDirection: LayoutDirection,
                                        popupContentSize: IntSize
                                    ): IntOffset {
                                        val offsetX = (screenWidth - popupContentSize.width) / 2
                                        val offsetY = (screenHeight - popupContentSize.height) / 2
//                                        val offsetX =
//                                            anchorBounds.left + (anchorBounds.width - popupContentSize.width) / 2
//                                        val offsetY = anchorBounds.top - 200
                                        return IntOffset(0, 0)
                                    }
                                },
                                onDismissRequest = { showCalendar = false }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(ColorTheme.backgroundColor, RoundedCornerShape(8.dp))
                                        .align(Alignment.Center)
                                        .padding(16.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.align(Alignment.Center)
                                    ) {
                                        WeekCalendar(newTask?.scheduledTime) { dayWeek ->
                                            if (newTask == null) {
                                                newTask = Task(title = "", description = "")
                                            }
                                            Logs.d("day:" + dayWeek.day)
                                            newTask?.scheduledTime = dayWeek.date
                                            showCalendar = false
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}