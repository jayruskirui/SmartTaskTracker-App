package com.jay.smarttasktracker

data class Task(
    val id: Int,
    val title: String,
    val completed: Boolean = false
)