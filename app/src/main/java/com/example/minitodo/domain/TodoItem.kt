package com.example.minitodo.domain

import java.time.LocalDateTime

data class TodoItem(
    val id: Int,
    val title: String,
    val timestamp: LocalDateTime
)
