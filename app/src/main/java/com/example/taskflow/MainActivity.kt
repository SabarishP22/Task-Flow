package com.example.taskflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.entity.Task
import com.example.taskflow.ui.theme.TaskFlowTheme
import com.example.taskflow.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskFlowTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.systemBars,
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        TaskListScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun TaskListScreen(viewModel: TaskViewModel = hiltViewModel()) {
    val tasks by viewModel.tasks.collectAsState()
    var editingTask by remember { mutableStateOf<Task?>(null) }

    Column {
        InsertTask(
            viewModel = viewModel,
            editingTask = editingTask,
            onTaskUpdated = { editingTask = null } // Reset after update
        )

        LazyColumn {
            items(tasks) { task ->
                TaskItem(
                    task,
                    onDelete = { viewModel.deleteTask(task) },
                    onUpdate = { editingTask = task } // Set task for editing
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertTask(viewModel: TaskViewModel = hiltViewModel(), editingTask: Task? = null, onTaskUpdated: () -> Unit = {}) {
    var titleTxt by remember(editingTask?.id) { mutableStateOf(editingTask?.title ?: "") }
    var descriptionTxt by remember(editingTask?.id) { mutableStateOf(editingTask?.description ?: "") }
    var categoryTxt by remember(editingTask?.id) { mutableStateOf(editingTask?.category ?: "") }
    var priorityTxt by remember(editingTask?.id) { mutableStateOf(
        when (editingTask?.priority) {
            1 -> "High"
            2 -> "Medium"
            else -> "Low"
        }
    ) }

    val isEditing = editingTask != null

    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(
                text = if (isEditing) "Edit Task" else "Create Task",
                fontSize = 23.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            TextField(
                value = titleTxt,
                onValueChange = { titleTxt = it },
                label = { Text(text = "Title") },
                modifier = Modifier.fillMaxWidth().padding(top = 13.dp, start = 20.dp, end = 20.dp)
                    .clip(RoundedCornerShape(30.dp)),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )

            TextField(
                value = descriptionTxt,
                onValueChange = { descriptionTxt = it },
                label = { Text(text = "Description") },
                modifier = Modifier.fillMaxWidth().padding(top = 13.dp, start = 20.dp, end = 20.dp)
                    .clip(RoundedCornerShape(30.dp)),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )

            DropdownField(
                label = "Priority",
                options = listOf("High", "Medium", "Low"),
                selectedOption = priorityTxt,
                onOptionSelected = { priorityTxt = it }
            )

            DropdownField(
                label = "Category",
                options = listOf( "Work", "Personal", "Health & Fitness", "Shopping",
                    "Finance", "Education", "Entertainment", "Travel",
                    "Home & Chores", "Social", "Meetings", "Hobbies", "Others"),
                selectedOption = categoryTxt,
                onOptionSelected = { categoryTxt = it }
            )

            val task = Task(
                id = editingTask?.id ?: 0,
                title = titleTxt,
                description = descriptionTxt,
                priority = when (priorityTxt) {
                    "High" -> 1
                    "Medium" -> 2
                    else -> 3
                },
                category = categoryTxt
            )

            Button(
                onClick = {
                    if (isEditing) {
                        viewModel.updateTask(task)
                    } else {
                        viewModel.insertTask(task)
                    }
                    onTaskUpdated() // Reset state after updating
                    titleTxt = ""
                    descriptionTxt = ""
                    priorityTxt = ""
                    categoryTxt = ""
                },
                modifier = Modifier.align(Alignment.CenterHorizontally).width(150.dp).padding(top = 10.dp)
            ) {
                Text(text = if (isEditing) "Update Task" else "Add Task")
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onDelete: () -> Unit, onUpdate: () -> Unit) {
    val priority = task.priority
    var color = if (priority == 1) Color.Red else if (priority == 2) Color.Blue else Color.Green
    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Text(task.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            Text(task.description ?: "", fontSize = 14.sp, color = Color.Gray)
            Row {
                Button(onClick = onDelete) { Text("Delete") }
                Button(onClick = onUpdate, modifier = Modifier.padding(start = 15.dp)) { Text("Update") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            TextField(
                value = selectedOption,
                onValueChange = {},
                label = { Text(text = label) },
                readOnly = true,
                modifier = Modifier.fillMaxWidth().padding(top = 13.dp, start = 20.dp, end = 20.dp)
                    .clip(RoundedCornerShape(30.dp)),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Dropdown Arrow"
                        )
                    }
                }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth().heightIn(max = 250.dp)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}