package me.crazystone.minihabits.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 任务实体，记录任务标题、描述、状态等信息
 */
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,                   // 任务标题（必填）
    val description: String? = null,       // 任务描述（可选）
    val isCompleted: Boolean = false,      // 任务状态，默认为未完成
    val createdTime: Long = System.currentTimeMillis() // 创建时间，默认当前时间
)