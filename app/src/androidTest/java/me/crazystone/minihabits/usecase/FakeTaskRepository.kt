package me.crazystone.minihabits.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.data.repository.TaskRepository

class FakeTaskRepository : TaskRepository {

    private val tasks = MutableStateFlow<List<Task>>(emptyList())

    override fun getTasks(): Flow<List<Task>> = tasks

    override suspend fun addTask(task: Task) {
        tasks.value = tasks.value + task
    }

    override suspend fun updateTask(task: Task) {
        tasks.value = tasks.value.map { if (it.id == task.id) task else it }
    }

    override suspend fun deleteTask(task: Task) {
        tasks.value = tasks.value.filterNot { it.id == task.id }
    }
}