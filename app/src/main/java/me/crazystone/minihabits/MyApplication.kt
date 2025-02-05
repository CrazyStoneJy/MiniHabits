package me.crazystone.minihabits

import android.app.Application
import me.crazystone.minihabits.data.database.AppDatabase
import me.crazystone.minihabits.data.repository.TaskRepository
import me.crazystone.minihabits.data.repository.TaskRepositoryImpl

class MyApplication : Application() {

    // 懒加载数据库与 Repository
    val database by lazy { AppDatabase.getDatabase(this) }
    val taskRepository: TaskRepository by lazy { TaskRepositoryImpl(database.taskDao()) }
}