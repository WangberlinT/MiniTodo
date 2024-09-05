package com.example.minitodo.domain

import com.example.minitodo.data.TodoItemDto
import com.example.minitodo.data.TodoItemInfo

interface TodoItemsRepository {
    suspend fun loadTodoItems(limit: Int, offset: Int): List<TodoItemDto>
    suspend fun deleteTodoItemById(id: Int): Boolean
    suspend fun insertTodoItem(item: TodoItemInfo)
    suspend fun insertTodoItems(items: List<TodoItemInfo>)
    suspend fun deleteAll()
}