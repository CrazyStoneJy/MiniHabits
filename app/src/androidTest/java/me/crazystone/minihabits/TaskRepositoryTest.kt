package me.crazystone.minihabits

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import me.crazystone.minihabits.data.database.AppDatabase
import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.data.repository.TaskRepository
import me.crazystone.minihabits.data.repository.TaskRepositoryImpl
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskRepositoryTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: TaskRepository

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        repository = TaskRepositoryImpl(database.taskDao())
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun addTaskTest() = runBlocking {
        val task = Task(title = "Repo Test", description = "Testing repository add")
        repository.addTask(task)

        val tasks = repository.getTasks().first()
        assertEquals(1, tasks.size)
        assertEquals("Repo Test", tasks.first().title)
    }

    @Test
    fun updateTaskTest() = runBlocking {
        val task = Task(title = "Repo Test", description = "Testing repository update")
        repository.addTask(task)

        var tasks = repository.getTasks().first()
        val insertedTask = tasks.first()

        val updatedTask = insertedTask.copy(isCompleted = true)
        repository.updateTask(updatedTask)

        tasks = repository.getTasks().first()
        assertTrue(tasks.first().isCompleted)
    }

    @Test
    fun deleteTaskTest() = runBlocking {
        val task = Task(title = "Repo Test", description = "Testing repository delete")
        repository.addTask(task)

        var tasks = repository.getTasks().first()
        assertEquals(1, tasks.size)

        val insertedTask = tasks.first()
        repository.deleteTask(insertedTask)

        tasks = repository.getTasks().first()
        assertTrue(tasks.isEmpty())
    }
}