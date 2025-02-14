package me.crazystone.minihabits.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.domain.provider.UseCaseProvider
import me.crazystone.minihabits.domain.usecase.AddTaskUseCase
import me.crazystone.minihabits.domain.usecase.DeleteTaskUseCase
import me.crazystone.minihabits.domain.usecase.GetTasksUseCase
import me.crazystone.minihabits.domain.usecase.UpdateTaskUseCase
import me.crazystone.minihabits.utils.Dates


class TaskViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(
                application,
                UseCaseProvider.getTasksUseCase,
                UseCaseProvider.addTaskUseCase,
                UseCaseProvider.updateTaskUseCase,
                UseCaseProvider.deleteTaskUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class TaskViewModel(
    application: Application,
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : AndroidViewModel(application) {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    private val _incompleteTasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks = _tasks.asStateFlow()
    val incompleteTasks = _incompleteTasks.asStateFlow()

    init {
        loadTasks()
        loadIncompleteTasks()
    }

    private fun sortTasks(list: List<Task>): List<Task> {
        val incompleteTasks = list.filter { !it.isCompleted }
        val completedTask = list.filter { it.isCompleted }
        val todayIncompleteTasks = incompleteTasks.filter {
            Dates.isToday(it.scheduledTime)
        }
        val otherDayIncompleteTasks = incompleteTasks.filter {
            !Dates.isToday(it.scheduledTime)
        }
        return todayIncompleteTasks
            .sortedByDescending { it.scheduledTime } +
                otherDayIncompleteTasks.sortedBy {
                    it.scheduledTime
                } +
                completedTask.sortedBy {
                    it.scheduledTime
                }
    }

    private fun loadIncompleteTasks() {
        viewModelScope.launch {
            getTasksUseCase()
                .collect {
                    _incompleteTasks.value =
                        it.filter { !it.isCompleted && Dates.isAfterToday(it.scheduledTime) }
                }
        }
    }

    private fun loadTasks() {
        viewModelScope.launch {
            getTasksUseCase()
                .map { list ->
                    list.filter {
                        !Dates.isBeforeToday(it.scheduledTime)
                    }
                }
                .collect {
                    // sort
                    _tasks.value = sortTasks((it))
                }
        }
    }

    fun addTask(title: String, description: String) {
        viewModelScope.launch {
            try {
                addTaskUseCase(
                    Task(
                        title = title,
                        description = description,
                        isRepeat = false,
                        repeatType = 0
                    )
                )
            } catch (e: IllegalArgumentException) {
                // 处理错误（例如：通知 UI）
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            try {
                addTaskUseCase(task)
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