package me.crazystone.minihabits.ui.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.ui.theme.ColorTheme
import me.crazystone.minihabits.ui.theme.Dimensions
import me.crazystone.minihabits.ui.viewmodel.HistoryTodoViewModel
import me.crazystone.minihabits.ui.viewmodel.TaskViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryList(viewModel: HistoryTodoViewModel) {
    val historyMap by viewModel.historyMap.collectAsState()

    val listState = rememberLazyListState()

    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
        historyMap.keys.forEach { key ->
            // 添加 stickyHeader 作为组标题
            stickyHeader {
                SectionHeader(title = key)
            }

            // 遍历子项
            items(historyMap[key]!!) { item ->
                TaskItem(task = item)
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun TaskItem(task: Task) {
    // 定义一个布尔类型状态，用于表示 checkbox 是否选中
    val isChecked = task.isCompleted

    Row(
        modifier = Modifier
            .fillMaxWidth(),
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
            Text(
                text = task.title,
                textDecoration = if (isChecked) TextDecoration.LineThrough else null,
                fontSize = Dimensions.TEXT_BODY_SIZE.sp
            )
        }
    }
}