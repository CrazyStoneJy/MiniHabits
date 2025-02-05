package me.crazystone.minihabits.domain.usecase

import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.data.repository.TaskRepository

class UpdateTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) {
        repository.updateTask(task)
    }
}