package me.crazystone.minihabits.domain.provider

import me.crazystone.minihabits.data.repository.TaskRepository
import me.crazystone.minihabits.domain.usecase.AddTaskUseCase
import me.crazystone.minihabits.domain.usecase.DeleteTaskUseCase
import me.crazystone.minihabits.domain.usecase.GetTasksUseCase
import me.crazystone.minihabits.domain.usecase.TaskUseCases
import me.crazystone.minihabits.domain.usecase.UpdateTaskUseCase

class UseCaseProvider(repository: TaskRepository) {
    val taskUseCases = TaskUseCases(
        addTask = AddTaskUseCase(repository),
        getTasks = GetTasksUseCase(repository),
        updateTask = UpdateTaskUseCase(repository),
        deleteTask = DeleteTaskUseCase(repository)
    )
}