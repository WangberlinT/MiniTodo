package com.example.minitodo.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.minitodo.di.Injector
import com.example.minitodo.ui.theme.MiniTodoTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    modifier: Modifier,
    viewModel: TodoScreenViewModel
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var isSheetOpen by remember {
        mutableStateOf(false)
    }
    val items by viewModel.items.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val hasMoreItems by viewModel.hasMoreItems.collectAsState()

    // Main content of the screen
    Box(modifier = modifier) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .align(Alignment.TopCenter)
        ) {
            TodoList(
                items = items,
                hasMoreData = hasMoreItems,
                onReachTheBottom = {
                    viewModel.loadMoreItems()
                },
                onDeleteItem = {
                    viewModel.deleteItemById(it.id)
                }
            )

        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    isSheetOpen = true
                }
            ) {
                Text(text = "Add")
            }

            Button(onClick = {
                viewModel.insert2000Items()
            }) {
                Text(text = "Insert 2000 items")
            }

            Button(onClick = {
                viewModel.deleteAll()
            }) {
                Text(text = "Clear all")
            }
        }

        val hideBottomSheet: () -> Unit = {
            scope.launch { sheetState.hide() }
                .invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        isSheetOpen = false
                    }
                }
        }

        if (isSheetOpen) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { isSheetOpen = false }
            ) {
                AddTodoUi (
                    onDismiss = {
                        hideBottomSheet()
                    },
                    onAddTodo = {
                        viewModel.addItem(it)
                        hideBottomSheet()
                    }
                )
            }
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

    }

}