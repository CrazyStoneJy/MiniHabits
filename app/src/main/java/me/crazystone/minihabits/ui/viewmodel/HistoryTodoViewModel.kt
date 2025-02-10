package me.crazystone.minihabits.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.domain.usecase.GetTasksUseCase
import me.crazystone.minihabits.utils.Dates

class HistoryTodoViewModel(private val getTasksUseCase: GetTasksUseCase) : ViewModel() {

    private val _map = MutableStateFlow<Map<String, MutableList<Task>>>(mutableMapOf())
    val historyMap = _map.asStateFlow()

    init {
        loadHistoryTasks()
    }

    private fun loadHistoryTasks() {
        viewModelScope.launch {
            getTasksUseCase()
                .map { list ->
                    list.filter {
                        Dates.isBeforeToday(it.scheduledTime)
                    }
                }
                .collect {
                    // assemble data info
                    val map: MutableMap<String, MutableList<Task>> = mutableMapOf()
                    it.forEach { item ->
                        val key = Dates.getDate(item.scheduledTime)
                        if (map[key] == null) {
                            map[key] = mutableListOf<Task>()
                        }
                        map[key]?.add(item)
                    }
                    _map.value = map
                }
        }
    }

}