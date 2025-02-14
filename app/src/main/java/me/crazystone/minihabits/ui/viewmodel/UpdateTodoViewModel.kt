package me.crazystone.minihabits.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.crazystone.minihabits.data.model.Task
import me.crazystone.minihabits.utils.Dates

class UpdateTodoViewModel() : ViewModel() {

    private val _map = MutableStateFlow<Map<String, MutableList<Task>>>(mutableMapOf())
    val historyMap = _map.asStateFlow()

}