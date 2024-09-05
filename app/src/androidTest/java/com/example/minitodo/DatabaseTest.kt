package com.example.minitodo

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.minitodo.data.TodoDatabaseHelper
import com.example.minitodo.data.TodoItemInfo
import com.example.minitodo.model.TodoItem
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.time.LocalDateTime

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var mTodoDatabase: TodoDatabaseHelper
    @Before
    fun setUp() {
        mTodoDatabase = TodoDatabaseHelper(InstrumentationRegistry.getInstrumentation().targetContext)
    }
    @After
    fun finish() {
        mTodoDatabase.deleteAll()
        mTodoDatabase.close()
    }

    @Test
    fun insert1TodoItems() {
        // Context of the app under test.
        val todoItem = TodoItemInfo("Item 1", LocalDateTime.now())
        mTodoDatabase.insertItem(todoItem)
        val items = mTodoDatabase.loadAll()
        assertTrue(items.size == 1)
        assertTrue(items[0].title == todoItem.title && items[0].timestamp == todoItem.timestamp.toString())
    }

    @Test
    fun insert50ItemsAndGetItemsWithLimit20Offset0() {
        val items = (1..50).map { TodoItemInfo("Item $it", LocalDateTime.now()) }
        items.forEach {
            mTodoDatabase.insertItem(it)
        }
        val limit = 20
        val page1 = mTodoDatabase.loadTodoItems(limit, 0)
        assertTrue(page1.size == 20)
        val page1Info = page1.map {
            TodoItemInfo(it.title, LocalDateTime.parse(it.timestamp))
        }
        assertTrue(page1Info == items.subList(0, limit))
    }

    @Test
    fun insert50ItemsAndGetItemsWithLimit20Offset40() {
        val items = (1..50).map { TodoItemInfo("Item $it", LocalDateTime.now()) }
        items.forEach {
            mTodoDatabase.insertItem(it)
        }
        val limit = 20
        val page1 = mTodoDatabase.loadTodoItems(limit, 40)
        assertTrue(page1.size == 10)
        val page1Info = page1.map {
            TodoItemInfo(it.title, LocalDateTime.parse(it.timestamp))
        }
        assertTrue(page1Info == items.subList(40, items.size))
    }

    @Test
    fun insert10ItemsAndDeleteItemWithIndex2() {
        val items = (1..10).map { TodoItemInfo("Item $it", LocalDateTime.now()) }
        items.forEach {
            mTodoDatabase.insertItem(it)
        }
        val all = mTodoDatabase.loadAll()
        mTodoDatabase.deleteItem(all[2].id)
        val updatedItems = items.toMutableList().apply { removeAt(2) }
        val itemsInDb = mTodoDatabase.loadAll().map {
            TodoItemInfo(it.title, LocalDateTime.parse(it.timestamp))
        }
        assertTrue(updatedItems == itemsInDb)

    }
}