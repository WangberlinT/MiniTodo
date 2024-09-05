package com.example.minitodo.model

import java.time.LocalDateTime

data class TodoItem(
    val id: Int,
    val title: String,
    val timestamp: LocalDateTime
)
