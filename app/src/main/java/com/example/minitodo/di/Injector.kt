package com.example.minitodo.di

import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.example.minitodo.ui.TodoScreenViewModel
import com.example.minitodo.ui.TodoScreenViewModelFactory

/**
 * Class that handles object creation
 * for simplicity no extra dependencies are added for dependency injection
 * In production environment, Dagger Hilt or Koin are popular for DI
 * Dagger Hilt: base on Dagger 2 and provide compile time dependency injection
 * Koin: Base on reflection, runtime injection
 */
object Injector {

    fun provideTodoScreenViewModelFactory(owner: SavedStateRegistryOwner) : TodoScreenViewModelFactory {
        return TodoScreenViewModelFactory(owner)
    }
}