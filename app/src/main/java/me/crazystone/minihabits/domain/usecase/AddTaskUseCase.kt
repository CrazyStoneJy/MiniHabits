package me.crazystone.minihabits.domain.usecase

import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.data.repository.TaskRepository

class AddTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) {
        if (task.title.isBlank()) {
            throw IllegalArgumentException("任务标题不能为空")
        }
        repository.addTask(task)
    }
}