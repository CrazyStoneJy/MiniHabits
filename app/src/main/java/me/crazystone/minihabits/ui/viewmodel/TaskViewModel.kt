package me.crazystone.minihabits.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.domain.usecase.AddTaskUseCase
import me.crazystone.minihabits.domain.usecase.DeleteTaskUseCase
import me.crazystone.minihabits.domain.usecase.GetTasksUseCase
import me.crazystone.minihabits.domain.usecase.UpdateTaskUseCase

class TaskViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks = _tasks.asStateFlow()

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            getTasksUseCase().collect { _tasks.value = it }
        }
    }

    fun addTask(title: String, description: String) {
        viewModelScope.launch {
            try {
                addTaskUseCase(Task(title = title, description = description))
            } catch (e: IllegalArgumentException) {
                // 处理错误（例如：通知 UI）
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            updateTaskUseCase(task)
            loadTasks()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            deleteTaskUseCase(task)
        }
    }
}