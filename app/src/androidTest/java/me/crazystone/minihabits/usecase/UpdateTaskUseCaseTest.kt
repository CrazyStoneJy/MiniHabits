package me.crazystone.minihabits.usecase

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.domain.usecase.UpdateTaskUseCase
import org.junit.Test

class UpdateTaskUseCaseTest {

    private val repository = FakeTaskRepository()
    private val updateTaskUseCase = UpdateTaskUseCase(repository)

    @Test
    fun updateTaskSuccessfully() = runBlocking {
        val task = Task(id = 1, title = "Old Task")
        repository.addTask(task)

        val updatedTask = task.copy(title = "Updated Task")
        updateTaskUseCase(updatedTask)

        val tasks = repository.getTasks().first()
        assertEquals("Updated Task", tasks.first().title)
    }
}