package me.crazystone.minihabits.usecase

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.domain.usecase.AddTaskUseCase
import org.junit.Assert.assertThrows
import org.junit.Test

class AddTaskUseCaseTest {

    private val repository = FakeTaskRepository()
    private val addTaskUseCase = AddTaskUseCase(repository)

    @Test
    fun addTaskSuccessfully() = runBlocking {
        val task = Task(title = "Test Task", description = "Task description")
        addTaskUseCase(task)

        val tasks = repository.getTasks().first()
        assertEquals(1, tasks.size)
        assertEquals("Test Task", tasks.first().title)
    }

    @Test
    fun addTaskWithEmptyTitleThrowsException() {
        val task = Task(title = "", description = "Invalid task")

        assertThrows(IllegalArgumentException::class.java) {
            runBlocking { addTaskUseCase(task) }
        }
    }
}