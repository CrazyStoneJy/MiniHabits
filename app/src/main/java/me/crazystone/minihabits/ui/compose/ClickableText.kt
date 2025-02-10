package me.crazystone.minihabits.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.crazystone.minihabits.ui.theme.Dimensions
import me.crazystone.minihabits.utils.Logs

@Composable
fun ClickableText(defaultText: String, textDecoration: TextDecoration?, onFocusChanged: (text: String) -> Unit) {
    var text by remember { mutableStateOf(defaultText) } // 用于保存文本的状态
    var isEditing by remember { mutableStateOf(false) } // 判断是否处于编辑模式
    var isFocused by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(isEditing) {
        if (isEditing) {
            focusRequester.requestFocus() // 请求焦点
        }
    }

    // 焦点改变时的处理
    val onFocusChanged: (Boolean) -> Unit = { hasFocus ->
        isFocused = hasFocus
        if (!hasFocus) {
            isEditing = false // 失去焦点时，退出编辑模式
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        if (isEditing) {
            // 如果处于编辑状态，显示 TextField
            TextField(
                value = text,
                onValueChange = {
                    text = it
                },
                modifier = Modifier.fillMaxWidth()
                    .focusRequester(focusRequester)  // Attach focusRequester
                    .onFocusChanged { focusState ->
                        onFocusChanged(focusState.isFocused)
                    },
                label = { Text("Enter Text") },
                singleLine = false,
            )
        } else {
            Text(
                text = text,
                fontSize = Dimensions.TEXT_BODY_SIZE.sp,
                textDecoration = textDecoration,
                modifier = Modifier
                    .clickable {
                        isEditing = true
                    } // 点击后进入编辑状态
                    .padding(8.dp)
            )
        }
    }
}