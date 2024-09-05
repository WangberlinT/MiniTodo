package com.example.minitodo.domain

import com.example.minitodo.data.TodoItemInfo
import java.time.LocalDateTime

class Insert2000TodoItemsUseCase(
    private val repository: TodoItemsRepository
) {
    // generate 2000 items
    suspend fun insertTodoItems() {
        val start = LocalDateTime.now().minusYears(1)
        val items = (1..2000).map {
            TodoItemInfo("Test Task $it", start.plusDays(it.toLong()))
        }
        repository.insertTodoItems(items)
    }
}