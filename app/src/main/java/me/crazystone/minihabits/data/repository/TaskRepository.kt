package me.crazystone.minihabits.data.repository

import kotlinx.coroutines.flow.Flow
import me.crazystone.minihabits.net.ChatCompletionResponse
import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.net.ChatBody

/**
 * 任务仓库接口，定义数据操作方法
 */
interface TaskRepository {
    fun getTasks(): Flow<List<Task>>
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
}