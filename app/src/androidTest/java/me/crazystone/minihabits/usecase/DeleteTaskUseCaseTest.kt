package me.crazystone.minihabits.usecase

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.domain.usecase.DeleteTaskUseCase
import org.junit.Test

class DeleteTaskUseCaseTest {

    private val repository = FakeTaskRepository()
    private val deleteTaskUseCase = DeleteTaskUseCase(repository)

    @Test
    fun deleteTaskSuccessfully() = runBlocking {
        val task = Task(id = 1, title = "Task to delete", repeatType = 0, isRepeat = false)
        repository.addTask(task)

        deleteTaskUseCase(task)

        val tasks = repository.getTasks().first()
        assertEquals(0, tasks.size)
    }
}