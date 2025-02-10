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
    var title: String,                   // 任务标题（必填）
    val description: String? = null,       // 任务描述（可选）
    val isCompleted: Boolean = false,      // 任务状态，默认为未完成
    val isAI: Boolean = false,            // 是否是ai生成的选项
    val createdTime: Long = System.currentTimeMillis(), // 创建时间，默认当前时间
    val updateTime: Long = 0,  // 任务状态更新时间
    // 想要完成的时间段
    var scheduledTime: Long = System.currentTimeMillis()
)