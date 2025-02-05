package me.crazystone.minihabits.domain.usecase

data class TaskUseCases(
    val addTask: AddTaskUseCase,
    val getTasks: GetTasksUseCase,
    val updateTask: UpdateTaskUseCase,
    val deleteTask: DeleteTaskUseCase
)