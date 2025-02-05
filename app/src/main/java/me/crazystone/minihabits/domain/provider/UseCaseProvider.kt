package me.crazystone.minihabits.domain.provider

import android.content.Context
import me.crazystone.minihabits.MyApplication
import me.crazystone.minihabits.data.repository.TaskRepository
import me.crazystone.minihabits.domain.usecase.AddTaskUseCase
import me.crazystone.minihabits.domain.usecase.DeleteTaskUseCase
import me.crazystone.minihabits.domain.usecase.GetTasksUseCase
import me.crazystone.minihabits.domain.usecase.UpdateTaskUseCase

object UseCaseProvider {
    private lateinit var taskRepository: TaskRepository

    lateinit var getTasksUseCase: GetTasksUseCase
        private set
    lateinit var addTaskUseCase: AddTaskUseCase
        private set
    lateinit var updateTaskUseCase: UpdateTaskUseCase
        private set
    lateinit var deleteTaskUseCase: DeleteTaskUseCase
        private set

    fun init(context: Context) {
        taskRepository = (context as MyApplication).taskRepository// ✅ 传递 Application Context
        getTasksUseCase = GetTasksUseCase(taskRepository)
        addTaskUseCase = AddTaskUseCase(taskRepository)
        updateTaskUseCase = UpdateTaskUseCase(taskRepository)
        deleteTaskUseCase = DeleteTaskUseCase(taskRepository)
    }
}