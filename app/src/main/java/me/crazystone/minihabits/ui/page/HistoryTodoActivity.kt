package me.crazystone.minihabits.ui.page

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.crazystone.minihabits.domain.provider.UseCaseProvider
import me.crazystone.minihabits.ui.compose.HistoryList
import me.crazystone.minihabits.ui.theme.ColorTheme
import me.crazystone.minihabits.ui.theme.Dimensions
import me.crazystone.minihabits.ui.viewmodel.HistoryTodoViewModel

class HistoryTodoActivity : ComponentActivity() {

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

    val context = LocalContext.current
    Scaffold(
        modifier = Modifier
            .background(ColorTheme.backgroundColor),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "History",
                        color = ColorTheme.primaryColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = Dimensions.TEXT_LARGE_SIZE.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { (context as Activity).finish() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
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


