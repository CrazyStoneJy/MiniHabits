package me.crazystone.minihabits

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import me.crazystone.minihabits.data.dao.TaskDao
import me.crazystone.minihabits.data.database.AppDatabase
import me.crazystone.minihabits.data.model.Task
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var taskDao: TaskDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        taskDao = database.taskDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertTaskAndGetAllTasks() = runBlocking {
        val task = Task(title = "Test Task", description = "This is a test task")
        taskDao.insertTask(task)

        val tasks = taskDao.getAllTasks().first()

        assertEquals(1, tasks.size)
        assertEquals("Test Task", tasks.first().title)
        assertEquals("This is a test task", tasks.first().description)
        assertFalse(tasks.first().isCompleted)
    }

    @Test
    fun updateTask() = runBlocking {
        val task = Task(title = "Test Task", description = "Update test")
        taskDao.insertTask(task)

        var tasks = taskDao.getAllTasks().first()
        val insertedTask = tasks.first()

        val updatedTask = insertedTask.copy(isCompleted = true)
        taskDao.updateTask(updatedTask)

        tasks = taskDao.getAllTasks().first()
        assertTrue(tasks.first().isCompleted)
    }

    @Test
    fun deleteTask() = runBlocking {
        val task = Task(title = "Test Task", description = "Delete test")
        taskDao.insertTask(task)

        var tasks = taskDao.getAllTasks().first()
        assertEquals(1, tasks.size)

        val insertedTask = tasks.first()
        taskDao.deleteTask(insertedTask)

        tasks = taskDao.getAllTasks().first()
        assertTrue(tasks.isEmpty())
    }
}