package me.crazystone.minihabits.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import me.crazystone.minihabits.data.model.Task

@Dao
interface TaskDao {

    /**
     * 查询所有任务，按创建时间倒序排序
     */
    @Query("SELECT * FROM tasks ORDER BY scheduledTime DESC")
    fun getAllTasks(): Flow<List<Task>>

    /**
     * 插入任务，若任务已存在则替换
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    /**
     * 更新任务信息（例如更新任务状态）
     */
    @Update
    suspend fun updateTask(task: Task)

    /**
     * 删除指定任务
     */
    @Delete
    suspend fun deleteTask(task: Task)
}