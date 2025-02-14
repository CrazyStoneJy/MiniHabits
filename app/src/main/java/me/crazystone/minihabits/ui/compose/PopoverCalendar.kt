package me.crazystone.minihabits.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.ui.theme.ColorTheme
import me.crazystone.minihabits.utils.DayWeek

@Composable
fun PopoverCalendar(
    isShow: Boolean,
    task: Task?,
    offset: IntOffset?,
    onDateClick: (dayWeek: DayWeek) -> Unit,
    onDismissRequest: () -> Unit,
) {
    if (isShow) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(dismissOnClickOutside = true) // 点击外部不关闭
        ) {
            Box(
                modifier = Modifier
                    .background(
                        ColorTheme.primaryColor,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                WeekCalendar(task?.scheduledTime) { dayWeek ->
                    onDateClick(dayWeek)
                }
            }
        }
    }

//    Box {
//        if (isShow) {
//            Popup(
//                popupPositionProvider = object : PopupPositionProvider {
//                    override fun calculatePosition(
//                        anchorBounds: IntRect,
//                        windowSize: IntSize,
//                        layoutDirection: LayoutDirection,
//                        popupContentSize: IntSize
//                    ): IntOffset {
//                        return offset ?: IntOffset(0, 0)
//                    }
//                },
//
//                onDismissRequest = {
//                    onDismissRequest()
//                }
//            ) {
//                Box(
//                    modifier = Modifier
//                        .background(
//                            ColorTheme.primaryColor,
//                            RoundedCornerShape(8.dp)
//                        ),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .align(Alignment.Center)
//                            .padding(16.dp)
//                    ) {
//                        WeekCalendar(task?.scheduledTime) { dayWeek ->
//                            onDateClick(dayWeek)
//                        }
//                    }
//                }
//            }
//        }
//    }
}