package com.example.minitodo.data

import java.time.LocalDateTime

data class TodoItemDto(
    val id: Int,
    val title: String,
    val timestamp: String
)

data class TodoItemInfo(
    val title: String,
    val timestamp: LocalDateTime
)
