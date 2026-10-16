package com.jay.smarttasktracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                TaskTrackerApp()
            }
        }
    }
}

@Composable
fun TaskTrackerApp() {
    var taskText by rememberSaveable {
        mutableStateOf("")
    }

    val tasks = remember {
        mutableStateListOf<Task>()
    }

    var nextId by remember {
        mutableIntStateOf(1)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(
            text = "My Tasks",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = taskText,
            onValueChange = { taskText = it },
            label = { Text("Enter a task") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val title = taskText.trim()

                if (title.isNotEmpty()) {
                    tasks.add(
                        Task(
                            id = nextId,
                            title = title
                        )
                    )

                    nextId++
                    taskText = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Task")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Your tasks: ${tasks.size}",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (tasks.isEmpty()) {
            Text(
                text = "No tasks yet. Add your first task!",
                color = Color.Gray
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tasks, key = { it.id }) { task ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = task.completed,
                            onCheckedChange = { checked ->
                                val index = tasks.indexOfFirst {
                                    it.id == task.id
                                }

                                if (index != -1) {
                                    tasks[index] =
                                        tasks[index].copy(
                                            completed = checked
                                        )
                                }
                            }
                        )

                        Text(
                            text = task.title,
                            modifier = Modifier.weight(1f),
                            textDecoration =
                                if (task.completed) {
                                    TextDecoration.LineThrough
                                } else {
                                    TextDecoration.None
                                },
                            color = Color.Black
                        )

                        TextButton(
                            onClick = {
                                tasks.removeAll {
                                    it.id == task.id
                                }
                            }
                        ) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}
