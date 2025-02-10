package me.crazystone.minihabits.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.crazystone.minihabits.data.dao.TaskDao
import me.crazystone.minihabits.data.model.Task

/**
 * Room 数据库，管理任务实体和对应的 DAO
 */
@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // 提供 DAO 实例
    abstract fun taskDao(): TaskDao

    companion object {
        // 保证多线程下的可见性
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * 获取数据库实例，采用单例模式，确保整个应用只创建一个数据库对象
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "todo_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}