package me.crazystone.minihabits.ui.page

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.crazystone.minihabits.domain.provider.UseCaseProvider
import me.crazystone.minihabits.ui.FloatingButton
import me.crazystone.minihabits.ui.TaskList
import me.crazystone.minihabits.ui.compose.CustomModalDialog
import me.crazystone.minihabits.ui.compose.HistoryList
import me.crazystone.minihabits.ui.theme.ColorTheme
import me.crazystone.minihabits.ui.theme.Dimensions
import me.crazystone.minihabits.ui.viewmodel.HistoryTodoViewModel
import me.crazystone.minihabits.ui.viewmodel.TaskViewModel

class HistoryTodoActivity: ComponentActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HistoryTodoViewModel(
                    UseCaseProvider.getTasksUseCase
                ) as T
            }
        })[HistoryTodoViewModel::class.java]
        setContent { HistoryTaskScreen(viewModel) }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryTaskScreen(viewModel: HistoryTodoViewModel) {

    Scaffold(
        modifier = Modifier
            .background(ColorTheme.backgroundColor),
        topBar = {
            TopAppBar(
                title = { Text("History", color = ColorTheme.primaryColor, fontWeight = FontWeight.Bold, fontSize = Dimensions.TEXT_LARGE_SIZE.sp) },
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = Dimensions.HORIZONTAL_MARGIN.dp),
            ) {
                HistoryList(viewModel)
            }
        },
    )
}


