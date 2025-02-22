package com.example.minitodo.presentation

import android.util.Log
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.example.minitodo.domain.Insert2000TodoItemsUseCase
import com.example.minitodo.domain.TodoItemUseCase
import com.example.minitodo.domain.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TodoScreenViewModel(
    private val todoItemUseCase: TodoItemUseCase,
    private val insert2000TodoItemsUseCase: Insert2000TodoItemsUseCase
) : ViewModel() {
    companion object {
        private const val TAG = "TodoScreenViewModel"
    }

    private val _items = MutableStateFlow<List<TodoItem>>(emptyList())
    val items = _items.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _hasMoreItems = MutableStateFlow(true)
    val hasMoreItems = _hasMoreItems.asStateFlow()

    private val _totalCount = MutableStateFlow(0)
    val totalCount = _totalCount.asStateFlow()

    init {
        loadFirstPage()
        updateTotalCount()
    }

    private fun updateTotalCount() = viewModelScope.launch{
        _totalCount.update { todoItemUseCase.getTotalCount() }
    }


    private fun loadFirstPage() {
        viewModelScope.launch {
            loadingAndExecute {
                val firstPage = todoItemUseCase.load(offset = 0)
                _items.update {
                    firstPage.items
                }
                _hasMoreItems.value = firstPage.hasMoreItems
            }
        }
    }

    fun loadMoreItems() {
        if (!_hasMoreItems.value) return
        Log.d(TAG, "loadMoreItems")
        viewModelScope.launch {
            loadingAndExecute {
                val nextPage = todoItemUseCase.load(offset = _items.value.size)
                _hasMoreItems.value = nextPage.hasMoreItems
                if (nextPage.items.isEmpty()) {
                    return@loadingAndExecute
                }
                _items.update {
                    _items.value + nextPage.items
                }
            }
        }
    }

    fun addItem(title: String) {
        viewModelScope.launch {
            todoItemUseCase.addItem(title)
            _hasMoreItems.value = true
            loadMoreItems()
            updateTotalCount()
        }
    }

    fun deleteItemById(id: Int) {
        viewModelScope.launch {
            _items.update {
                _items.value.toMutableList().apply { removeIf { it.id == id} }
            }
            todoItemUseCase.deleteTodoItemById(id)
            updateTotalCount()
        }
    }

    fun insert2000Items() = viewModelScope.launch {
        loadingAndExecute {
            insert2000TodoItemsUseCase.insertTodoItems()
            _hasMoreItems.value = true
        }
        updateTotalCount()
        loadMoreItems()
    }

    fun deleteAll() = viewModelScope.launch {
        todoItemUseCase.deleteAll()
        loadFirstPage()
        updateTotalCount()
    }

    private suspend fun loadingAndExecute(block: suspend () -> Unit) {
        if (_isLoading.value) return
        _isLoading.update { true }
        block()
        _isLoading.update { false }
    }

}

class TodoScreenViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val loadTodoItemsUseCase: TodoItemUseCase,
    private val insert2000TodoItemsUseCase: Insert2000TodoItemsUseCase
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(TodoScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoScreenViewModel(loadTodoItemsUseCase, insert2000TodoItemsUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
