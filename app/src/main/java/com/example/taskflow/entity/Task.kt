package com.example.taskflow.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val priority: Int,       // 1-high, 2-medium, 3-low
    val category: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
