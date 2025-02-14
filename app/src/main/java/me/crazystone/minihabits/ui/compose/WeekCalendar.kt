package me.crazystone.minihabits.ui.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.crazystone.minihabits.ui.theme.ColorTheme
import me.crazystone.minihabits.ui.theme.Dimensions
import me.crazystone.minihabits.utils.Dates
import me.crazystone.minihabits.utils.DayWeek
import me.crazystone.minihabits.utils.Logs
import me.crazystone.minihabits.utils.UIExt

@SuppressLint("NewApi")
@Composable
fun WeekCalendar(defaultDate: Long?, onSelectDay: (dayWeek: DayWeek) -> Unit) {
    val dayWeeks = Dates.getDayOneWeek()
    var index = 0
    if (defaultDate != null) {
        Logs.d(Dates.getDate(defaultDate))
        Logs.d(dayWeeks)
        index = dayWeeks.indexOfFirst {
            it.dateString == Dates.getDate(defaultDate)
        }
    }

    var selectedIndex by remember { mutableStateOf(index) }
    var ym by remember { mutableStateOf(dayWeeks[selectedIndex].ym) }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
        Text(ym, fontSize = Dimensions.TEXT_MID_SIZE.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
        Spacer(Modifier.padding(4.dp))
        Row {
            dayWeeks.forEachIndexed { index, it ->
                var everWidth by remember { mutableStateOf(0F) }
                val context = LocalContext.current
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,  // 水平居中
                    modifier = Modifier
                        .weight(weight = 1F)
                        .onGloballyPositioned { layoutCoordinates ->
                            // 获取宽度并存储
                            everWidth = layoutCoordinates.size.width.toFloat()
                        }
                ) {
                    Text(it.week, fontSize = Dimensions.TEXT_MID_SIZE.sp)
                    Spacer(Modifier.padding(4.dp))
                    Column(
                        verticalArrangement = Arrangement.Center,  // 垂直居中
                        horizontalAlignment = Alignment.CenterHorizontally,  // 水平居中
                        modifier = Modifier
                            .size(
                                UIExt.pxToDp(
                                    context,
                                    everWidth
                                ).dp
                            )  // 设置 Row 的大小为正方形
                            .clip(CircleShape)  // 使 Row 成为圆形
                            .background(if (index == selectedIndex) ColorTheme.whiteColor else ColorTheme.primaryColor)  // 设置背景颜色
                            .clickable {
                                selectedIndex = index
                                ym = it.ym
                                onSelectDay(it)
                            }
                    ) {
                        Text(it.day, fontSize = Dimensions.TEXT_MID_SIZE.sp)
                        if (it.extra != null) {
                            Text(it.extra, color = ColorTheme.warningColor, fontSize = 8.sp)
                        }
                    }
                }
            }
        }
    }

}