package me.crazystone.minihabits.usecase

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.domain.usecase.GetTasksUseCase
import org.junit.Test

class GetTasksUseCaseTest {

    private val repository = FakeTaskRepository()
    private val getTasksUseCase = GetTasksUseCase(repository)

    @Test
    fun getTasksReturnsCorrectList() = runBlocking {
        val task1 = Task(title = "Task 1")
        val task2 = Task(title = "Task 2")
        repository.addTask(task1)
        repository.addTask(task2)

        val tasks = getTasksUseCase().first()

        assertEquals(2, tasks.size)
        assertEquals("Task 1", tasks[0].title)
        assertEquals("Task 2", tasks[1].title)
    }
}