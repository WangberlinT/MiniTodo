package com.example.minitodo.di

import android.app.Application
import androidx.savedstate.SavedStateRegistryOwner
import com.example.minitodo.data.TodoDatabaseHelper
import com.example.minitodo.data.TodoItemRepositoryImpl
import com.example.minitodo.domain.Insert2000TodoItemsUseCase
import com.example.minitodo.domain.TodoItemUseCase
import com.example.minitodo.domain.TodoItemMapper
import com.example.minitodo.domain.TodoItemsRepository
import com.example.minitodo.presentation.TodoScreenViewModelFactory
import kotlinx.coroutines.Dispatchers

/**
 * Class that handles object creation
 * for simplicity no extra dependencies are added for dependency injection
 * In production environment, Dagger Hilt or Koin are popular for DI
 * Dagger Hilt: base on Dagger 2 and provide compile time dependency injection
 * Koin: Base on reflection, runtime injection
 */
object Injector {
    private lateinit var applicationContext: Application
    private val todoDatabaseHelper: TodoDatabaseHelper by lazy {
        TodoDatabaseHelper(applicationContext)
    }

    fun init(application: Application) {
        applicationContext = application
    }

    fun provideTodoScreenViewModelFactory(owner: SavedStateRegistryOwner): TodoScreenViewModelFactory {
        return TodoScreenViewModelFactory(
            owner,
            provideLoadTodoItemsUseCase(),
            provideInsert2000TodoItemsUseCase()
        )
    }

    private fun provideLoadTodoItemsUseCase(): TodoItemUseCase {
        return TodoItemUseCase(provideTodoItemRepository(), provideTodoItemMapper())
    }

    private fun provideInsert2000TodoItemsUseCase() = Insert2000TodoItemsUseCase(
        provideTodoItemRepository()
    )

    private fun provideTodoItemMapper(): TodoItemMapper = TodoItemMapper()

    private fun provideTodoItemRepository(): TodoItemsRepository =
        TodoItemRepositoryImpl(Dispatchers.IO, provideTodoDatabaseHelper())

    /**
     * Singleton
     */
    private fun provideTodoDatabaseHelper(): TodoDatabaseHelper = todoDatabaseHelper
}