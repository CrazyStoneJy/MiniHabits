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
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.crazystone.minihabits.data.repository.TaskRepositoryImpl
import me.crazystone.minihabits.domain.provider.UseCaseProvider
import me.crazystone.minihabits.ui.TaskScreen
import me.crazystone.minihabits.ui.theme.MiniHabitsTheme
import me.crazystone.minihabits.ui.viewmodel.TaskViewModel
import me.crazystone.minihabits.ui.viewmodel.TaskViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = ViewModelProvider(
            this,
            TaskViewModelFactory(application)
        ).get(TaskViewModel::class.java)

        setContent {
            TaskScreen(viewModel)
        }
    }
}
