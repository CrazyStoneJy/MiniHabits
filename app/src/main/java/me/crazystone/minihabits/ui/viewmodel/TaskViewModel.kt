package me.crazystone.minihabits.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.domain.provider.UseCaseProvider
import me.crazystone.minihabits.domain.usecase.AddTaskUseCase
import me.crazystone.minihabits.domain.usecase.DeleteTaskUseCase
import me.crazystone.minihabits.domain.usecase.GetTasksUseCase
import me.crazystone.minihabits.domain.usecase.RequestChat
import me.crazystone.minihabits.domain.usecase.UpdateTaskUseCase
import me.crazystone.minihabits.net.ChatBody
import me.crazystone.minihabits.net.ChatCompletionResponse
import me.crazystone.minihabits.net.Message
import me.crazystone.minihabits.net.NetResultState
import me.crazystone.minihabits.net.ResponseFormat
import me.crazystone.minihabits.utils.Dates
import me.crazystone.minihabits.utils.Logs


class TaskViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(
                application,
                UseCaseProvider.getTasksUseCase,
                UseCaseProvider.addTaskUseCase,
                UseCaseProvider.updateTaskUseCase,
                UseCaseProvider.deleteTaskUseCase,
                UseCaseProvider.requestChatUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class TaskViewModel(
    application: Application,
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val chatRequest: RequestChat
) : AndroidViewModel(application) {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    private val _incompleteTasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks = _tasks.asStateFlow()
    val incompleteTasks = _incompleteTasks.asStateFlow()

    private val _chatResponse = MutableStateFlow<NetResultState<ChatCompletionResponse>?>(null)
    val chatResponse = _chatResponse.asStateFlow()

    init {
        loadTasks()
        loadIncompleteTasks()
    }


    private fun createChatRequest(content: String): ChatBody {
        val systemPrompt = """
            The user will provide some exam text. Please split tasks according to the tomato work method and output them in JSON format. 

            EXAMPLE INPUT: 
            I want to finish todo app ui design.
            
            EXAMPLE JSON OUTPUT:
            [
                "plan ui design",
                "write compose code by ui design",
            ]
        """
        val messages = listOf(
            Message(content = systemPrompt, role = "system"),
            Message(content = content, role = "user")
        )

        val responseFormat = ResponseFormat(type = "text")

        return ChatBody(
            messages = messages,
            model = "deepseek-chat",
            frequencyPenalty = 0,
            maxTokens = 2048,
            presencePenalty = 0,
            responseFormat = responseFormat,
            stop = null,
            stream = false,
            streamOptions = null,
            temperature = 1,
            topP = 1,
            tools = null,
            toolChoice = "none",
            logprobs = false,
            topLogprobs = null
        )
    }

    fun getChatCompletion(content: String) {
        viewModelScope.launch {
            chatRequest(createChatRequest(content)).collect {
                Logs.d("getChatCompletion: $it")
                _chatResponse.value = it
            }
        }
    }

    fun clearChatResponse() {
        _chatResponse.value = null
    }

    private fun sortTasks(list: List<Task>): List<Task> {
        val incompleteTasks = list.filter { !it.isCompleted }
        val completedTask = list.filter { it.isCompleted }
        val todayIncompleteTasks = incompleteTasks.filter {
            Dates.isToday(it.scheduledTime)
        }
        val otherDayIncompleteTasks = incompleteTasks.filter {
            !Dates.isToday(it.scheduledTime)
        }
        return todayIncompleteTasks
            .sortedByDescending { it.scheduledTime } +
                otherDayIncompleteTasks.sortedBy {
                    it.scheduledTime
                } +
                completedTask.sortedBy {
                    it.scheduledTime
                }
    }

    fun loadIncompleteTasks() {
        viewModelScope.launch {
            getTasksUseCase()
                .collect {
                    _incompleteTasks.value =
                        it.filter { tasks ->
                            Dates.isBeforeToday(
                                tasks.scheduledTime
                            ) && (tasks.isRepeat || !tasks.isCompleted)
                        }
                }
        }
    }

    private fun loadTasks() {
        viewModelScope.launch {
            getTasksUseCase()
                .map { list ->
                    list.filter {
                        !Dates.isBeforeToday(it.scheduledTime)
                    }
                }
                .collect {
                    // sort
                    _tasks.value = sortTasks((it))
                }
        }
    }

    fun addTask(title: String, description: String) {
        viewModelScope.launch {
            try {
                addTaskUseCase(
                    Task(
                        title = title,
                        description = description,
                        isRepeat = false,
                        repeatType = 0
                    )
                )
            } catch (e: IllegalArgumentException) {
                // 处理错误（例如：通知 UI）
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            try {
                addTaskUseCase(task)
            } catch (e: IllegalArgumentException) {
                // 处理错误（例如：通知 UI）
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            updateTaskUseCase(task)
            loadTasks()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            deleteTaskUseCase(task)
        }
    }
}