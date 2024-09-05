package com.example.minitodo.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TodoDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "TodoDatabase.db", null, 1) {
    companion object {
        const val TABLE_NAME = "todo_items"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_TIMESTAMP = "timestamp"
    }

    /**
     * id (Primary key)
     * title TEXT
     * timestamp stored in ISO 8601 YYYY-MM-DDTHH:MM:SS
     */
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            """
                CREATE TABLE $TABLE_NAME (
                    $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_TITLE TEXT,
                    $COLUMN_TIMESTAMP TEXT
                );
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun loadTodoItems(limit: Int, offset: Int): List<TodoItemDto> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME LIMIT ? OFFSET ?",
            arrayOf(
                limit.toString(), offset.toString()
            )
        )
        val items = mutableListOf<TodoItemDto>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val timestampStr = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP))
                items.add(TodoItemDto(id, title, timestampStr))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return items
    }

    fun loadAll() : List<TodoItemDto> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME",
            null
        )
        val items = mutableListOf<TodoItemDto>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val timestampStr = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP))
                items.add(TodoItemDto(id, title, timestampStr))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return items
    }

    fun insertItem(item: TodoItemInfo) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_TITLE, item.title)
            put(COLUMN_TIMESTAMP, item.timestamp.toString())
        }
        db.insert(TABLE_NAME, null, contentValues)
    }

    fun insertItems(items: List<TodoItemInfo>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            for (item in items) {
                val values = ContentValues().apply {
                    put(COLUMN_TITLE, item.title)
                    put(COLUMN_TIMESTAMP, item.timestamp.toString())
                }
                db.insert(TABLE_NAME, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun deleteItem(id: Int) : Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun deleteAll() : Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, null, null)
    }
}