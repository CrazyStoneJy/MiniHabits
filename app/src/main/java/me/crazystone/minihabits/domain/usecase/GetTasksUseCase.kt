package me.crazystone.minihabits.domain.usecase

import kotlinx.coroutines.flow.Flow
import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.data.repository.TaskRepository

class GetTasksUseCase(private val repository: TaskRepository) {
    operator fun invoke(): Flow<List<Task>> = repository.getTasks()
}