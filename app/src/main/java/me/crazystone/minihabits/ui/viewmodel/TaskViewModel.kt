package me.crazystone.minihabits.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.domain.usecase.TaskUseCases

class TaskViewModel(private val taskUseCases: TaskUseCases) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks = _tasks.asStateFlow()

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            taskUseCases.getTasks().collect { _tasks.value = it }
        }
    }

    fun addTask(title: String, description: String) {
        viewModelScope.launch {
            try {
                taskUseCases.addTask(Task(title = title, description = description))
            } catch (e: IllegalArgumentException) {
                // 处理错误（例如：通知 UI）
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskUseCases.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskUseCases.deleteTask(task)
        }
    }
}