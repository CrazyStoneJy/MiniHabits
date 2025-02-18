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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.net.ChatCompletionResponse
import me.crazystone.minihabits.net.NetResultState
import me.crazystone.minihabits.ui.theme.ColorTheme
import me.crazystone.minihabits.ui.theme.Dimensions
import me.crazystone.minihabits.ui.viewmodel.TaskViewModel
import me.crazystone.minihabits.utils.Gsons.fromJsonToStringArray
import me.crazystone.minihabits.utils.Logs


@Composable
fun AIAssistantModal(
    showDialog: Boolean,
    viewModel: TaskViewModel,
    onConfirm: (tasks: List<Task>) -> Unit,
    onDismiss: () -> Unit
) {
    val chatResponseState by viewModel.chatResponse.collectAsState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Logs.d("chatResponse: " + chatResponseState.toString())
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(dismissOnClickOutside = false) // 点击外部不关闭
        ) {
            var title by remember { mutableStateOf("") }
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
                            "AI Assistant",
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
                        MHButton("ok") {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            if (title != "") {
                                viewModel.getChatCompletion(title)
                            } else {
                                Toast.makeText(context, "请输入title", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    when (chatResponseState) {
                        is NetResultState.Loading -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                LoadingView()
                            }
                        }

                        is NetResultState.Success -> {
                            val chatResponse =
                                (chatResponseState as NetResultState.Success<ChatCompletionResponse>).data
                            val selectedList = remember {
                                mutableStateListOf<Pair<Int, Task>>()
                            }
                            val choice = chatResponse.choices[0]
                            val content = choice.message.content
                            val items: List<String> =
                                fromJsonToStringArray(content)

                            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                                itemsIndexed(items) { index, item ->
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(vertical = 6.dp)
                                    ) {
                                        val isSelected = (selectedList.map { it.first }
                                            .contains(index))
                                        Text(
                                            item,
                                            fontSize = Dimensions.TEXT_LARGE_SIZE.sp,
                                            modifier = Modifier.weight(1F)
                                        )
                                        Icon(
                                            imageVector = if (isSelected) Icons.Default.Check else Icons.Default.Add,
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .clickable {
                                                    val storedIndex =
                                                        selectedList
                                                            .map { it.first }
                                                            .indexOf(index)
                                                    if (storedIndex < 0) {
                                                        Logs.d("storedIndex < 0")
                                                        selectedList.add(
                                                            Pair(
                                                                index,
                                                                Task(
                                                                    title = item,
                                                                    isRepeat = false,
                                                                    repeatType = 0,
                                                                    description = item,
                                                                    scheduledTime = System.currentTimeMillis()
                                                                )
                                                            )
                                                        )
                                                    } else {
                                                        selectedList.removeAt(storedIndex)
                                                    }
                                                    Logs.d("selectedList: ${selectedList.size}")
                                                },
                                            tint = if (isSelected) ColorTheme.blackColor else ColorTheme.primaryColor,
                                            contentDescription = "add",
                                        )
                                    }
                                }
                                item {
                                    Button(
                                        onClick = {
                                            onConfirm(selectedList.map { it.second })
                                        }, colors = ButtonDefaults.buttonColors(
                                            containerColor = ColorTheme.primaryColor
                                        )
                                    ) {
                                        Text(
                                            text = "confirm",
                                            fontSize = Dimensions.TEXT_LARGE_SIZE.sp,
                                            color = ColorTheme.blackColor,
                                        )
                                    }
                                }
                            }
                        }

                        is NetResultState.Error -> {
                            Text("Error: ${(chatResponseState as NetResultState.Error).message}")  // 显示错误信息
                        }

                        null -> {

                        }
                    }

                }
            }
        }
    }
}