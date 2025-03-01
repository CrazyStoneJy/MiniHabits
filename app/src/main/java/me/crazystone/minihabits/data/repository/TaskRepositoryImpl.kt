package me.crazystone.minihabits.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.crazystone.minihabits.data.dao.TaskDao
import me.crazystone.minihabits.net.ChatCompletionResponse
import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.net.ChatBody
import me.crazystone.minihabits.net.RetrofitInstance.apiService

/**
 * 任务仓库实现，负责调用 TaskDao 完成数据操作
 */
class TaskRepositoryImpl(private val taskDao: TaskDao) : TaskRepository {

    override fun getTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    override suspend fun addTask(task: Task) {
        taskDao.insertTask(task)
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

}