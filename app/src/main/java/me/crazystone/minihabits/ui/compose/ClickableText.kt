package me.crazystone.minihabits.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import me.crazystone.minihabits.ui.theme.ColorTheme

@Composable
fun ClickableText(
    index: Int,
    text: String,
    textDecoration: TextDecoration?,
    fontSize: TextUnit,
    focusRequesterMap: MutableMap<Int, FocusRequester>,
    focusedItemIndex: MutableState<Int>
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    var text by remember { mutableStateOf(text) } // 用于保存文本的状态

    val focusRequester = focusRequesterMap.getOrPut(index) { FocusRequester() }
//    val focusManager = LocalFocusManager.current

    Column {
        TextField(
            value = text,
            onValueChange = { newText ->
                text = newText
            },
//            textStyle = TextStyle(text),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
//                .onFocusChanged { focusState ->
//                    if (focusState.isFocused) {
//                        focusedItemIndex.value = index // 记录当前焦点位置
//                    } else if (focusedItemIndex.value == index) {
//                        focusedItemIndex.value = -1 // 失去焦点时重置
//                        Logs.d("un focus index: $index")
//                    }
//                }
                .clickable {
                    focusedItemIndex.value = index // 点击当前的 TextField，设置焦点
                    focusRequester.requestFocus()
                }
                .then(
                    // 为禁用状态自定义样式
                    if (focusedItemIndex.value != index) {
                        Modifier.background(ColorTheme.primaryColor) // 灰色背景
                            .border(1.dp, Color.LightGray) // 灰色边框
                    } else Modifier
                ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = ColorTheme.transparent,
                unfocusedContainerColor = ColorTheme.transparent,
                focusedIndicatorColor = ColorTheme.transparent,
                unfocusedIndicatorColor = ColorTheme.transparent
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide() // 关闭键盘
                }
            ),
            textStyle = TextStyle(fontSize = fontSize, textDecoration = textDecoration),
            enabled = focusedItemIndex.value == index,
        )
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }
}