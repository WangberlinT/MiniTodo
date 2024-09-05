package com.example.minitodo.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.minitodo.domain.TodoItem
import com.example.minitodo.ui.theme.MiniTodoTheme
import java.time.LocalDateTime
import java.util.UUID

private const val TAG = "TodoList"
@Composable
@Preview
fun PreviewTodoList() {
    MiniTodoTheme {
        val items = remember {
            (1..5).map { TodoItem(UUID.randomUUID().mostSignificantBits.toInt(), "Item $it", LocalDateTime.now()) }.toMutableStateList()
        }
        TodoList(
            items = items,
            hasMoreData = true,
            onReachTheBottom = {
                items.addAll(
                    (1+items.size..20 + items.size).map { TodoItem(UUID.randomUUID().mostSignificantBits.toInt(), "Item $it", LocalDateTime.now()) }
                )
            },
            onDeleteItem = {
                Log.d(TAG, "on delete: $it")
                items.remove(it)
            }
        )
    }

}

@Composable
fun TodoList(
    items: List<TodoItem>,
    hasMoreData: Boolean,
    onReachTheBottom: () -> Unit = {},
    onDeleteItem: (TodoItem) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(items.size, key = { items[it].id }) { index ->
            // Display each item
            TodoItemUi(
                modifier = Modifier.animateItem(),
                item = items[index],
                onDelete = onDeleteItem
            )
        }
        item {
            // the user has scrolled to the bottom of the list
            if (hasMoreData) {
                CircularProgressIndicator()
            }
            LaunchedEffect(true) {
                onReachTheBottom()
            }
        }
    }
}