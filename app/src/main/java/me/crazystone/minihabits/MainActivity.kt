package me.crazystone.minihabits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.crazystone.minihabits.data.repository.TaskRepositoryImpl
import me.crazystone.minihabits.domain.provider.UseCaseProvider
import me.crazystone.minihabits.ui.TaskScreen
import me.crazystone.minihabits.ui.theme.MiniHabitsTheme
import me.crazystone.minihabits.ui.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        UseCaseProvider.init(applicationContext)
        val viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TaskViewModel(
                    UseCaseProvider.getTasksUseCase,
                    UseCaseProvider.addTaskUseCase,
                    UseCaseProvider.updateTaskUseCase,
                    UseCaseProvider.deleteTaskUseCase
                ) as T
            }
        })[TaskViewModel::class.java]
        setContent {
            TaskScreen(viewModel)
        }
    }
}
