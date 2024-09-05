package com.example.minitodo.data

import com.example.minitodo.domain.TodoItemsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Facade pattern for data sources.
 * In this case only the sqlite database
 */
class TodoItemRepositoryImpl(
    private val workDispatcher: CoroutineDispatcher,
    private val todoDatabaseHelper: TodoDatabaseHelper
) : TodoItemsRepository{

    override suspend fun loadTodoItems(limit: Int, offset: Int): List<TodoItemDto> = withContext(workDispatcher) {
        todoDatabaseHelper.loadTodoItems(limit, offset)
    }

    override suspend fun getTotalCount(): Int = withContext(workDispatcher) {
        todoDatabaseHelper.getTotalCount()
    }

    override suspend fun deleteTodoItemById(id: Int): Boolean = withContext(workDispatcher) {
        todoDatabaseHelper.deleteItem(id) != 0
    }

    override suspend fun insertTodoItem(item: TodoItemInfo) = withContext(workDispatcher) {
        todoDatabaseHelper.insertItem(item)
    }

    override suspend fun insertTodoItems(items: List<TodoItemInfo>) = withContext(workDispatcher) {
        todoDatabaseHelper.insertItems(items)
    }

    override suspend fun deleteAll() = withContext(workDispatcher) {
        val count = todoDatabaseHelper.deleteAll()
    }
}