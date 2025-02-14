package me.crazystone.minihabits

import android.app.Application
import me.crazystone.minihabits.data.database.AppDatabase
import me.crazystone.minihabits.data.repository.TaskRepository
import me.crazystone.minihabits.data.repository.TaskRepositoryImpl
import me.crazystone.minihabits.domain.provider.UseCaseProvider

class MyApplication : Application() {

    // 懒加载数据库与 Repository
    private val database by lazy { AppDatabase.getDatabase(this) }
    val taskRepository: TaskRepository by lazy { TaskRepositoryImpl(database.taskDao()) }

    override fun onCreate() {
        super.onCreate()
        UseCaseProvider.init(this)
    }
}