package me.crazystone.minihabits.domain.provider

import android.content.Context
import me.crazystone.minihabits.MyApplication
import me.crazystone.minihabits.data.repository.ChatRepository
import me.crazystone.minihabits.data.repository.TaskRepository
import me.crazystone.minihabits.domain.usecase.AddTaskUseCase
import me.crazystone.minihabits.domain.usecase.DeleteTaskUseCase
import me.crazystone.minihabits.domain.usecase.GetTasksUseCase
import me.crazystone.minihabits.domain.usecase.RequestChat
import me.crazystone.minihabits.domain.usecase.UpdateTaskUseCase

object UseCaseProvider {
    private lateinit var taskRepository: TaskRepository
    private lateinit var chatRepository: ChatRepository

    lateinit var getTasksUseCase: GetTasksUseCase
        private set
    lateinit var addTaskUseCase: AddTaskUseCase
        private set
    lateinit var updateTaskUseCase: UpdateTaskUseCase
        private set
    lateinit var deleteTaskUseCase: DeleteTaskUseCase
        private set
    lateinit var requestChatUseCase: RequestChat
        private set

    fun init(context: Context) {
        taskRepository = (context as MyApplication).taskRepository// ✅ 传递 Application Context
        chatRepository = context.chatRepository
        getTasksUseCase = GetTasksUseCase(taskRepository)
        addTaskUseCase = AddTaskUseCase(taskRepository)
        updateTaskUseCase = UpdateTaskUseCase(taskRepository)
        deleteTaskUseCase = DeleteTaskUseCase(taskRepository)
        requestChatUseCase = RequestChat(chatRepository)
    }
}