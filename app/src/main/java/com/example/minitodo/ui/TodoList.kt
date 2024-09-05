package com.example.minitodo.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
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
            onLoadMore = {
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
    onLoadMore: () -> Unit = {},
    onDeleteItem: (TodoItem) -> Unit = {}
) {

    val lazyListState = rememberLazyListState()

    // Track when to load more items based on scrolling
    LaunchedEffect(lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index) {
        val index = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return@LaunchedEffect
        if (index >= items.size / 2) {
            onLoadMore()
        }
    }
    LazyColumn(
        state = lazyListState,
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
        }
    }
}