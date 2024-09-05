package com.example.minitodo.domain

import com.example.minitodo.data.TodoItemDto
import com.example.minitodo.data.TodoItemInfo
import java.time.LocalDateTime

class TodoItemUseCase(
    private val repository: TodoItemsRepository,
    private val mapper: TodoItemMapper
) {
    suspend fun getTotalCount() : Int {
        return repository.getTotalCount()
    }

    suspend fun load(limit: Int = 20, offset: Int): LoadedResult {
        val items = repository.loadTodoItems(limit, offset).map { mapper.mapTodoItemDtoToTodoItem(it) }
        return LoadedResult(
            items = items,
            hasMoreItems = items.size == limit
        )
    }

    suspend fun addItem(title: String) {
        val info = TodoItemInfo(title, LocalDateTime.now())
        repository.insertTodoItem(info)
    }

    suspend fun deleteTodoItemById(id: Int) {
        repository.deleteTodoItemById(id)
    }

    suspend fun deleteAll() {
        repository.deleteAll()
    }

    data class LoadedResult(
        val items: List<TodoItem>,
        val hasMoreItems: Boolean
    )

}

class TodoItemMapper {
    fun mapTodoItemDtoToTodoItem(dto: TodoItemDto) : TodoItem {
        return TodoItem(dto.id, dto.title, LocalDateTime.parse(dto.timestamp))
    }
}