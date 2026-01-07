// THE USER INTERFACE OF HOME SCREEN

package com.example.student_planner_project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import java.util.concurrent.TimeUnit
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.student_planner_project.data.models.Task
import com.example.student_planner_project.ui.viewmodels.MainViewModel

@Composable
fun HomeScreen(mainViewModel: MainViewModel) {
    val semester = mainViewModel.semester.collectAsState()
    val currentSemester = semester.value
    val totalTasks = currentSemester?.subjects?.sumOf { it.tasks.size }
    val selectedTask = remember {mutableStateOf<Task?>(null)}
    val task = selectedTask.value
    val totalSubjects = currentSemester?.subjects?.size

    val remainingDays =
        if(currentSemester != null){
            val time = currentSemester.endDate - System.currentTimeMillis()
            val days = TimeUnit.MILLISECONDS.toDays(time)

            maxOf(0, days)
        }else{
            0
        }
    val tasks = currentSemester?.subjects?.flatMap { it.tasks }
    val missedTasks = tasks?.filter { it.dueDate < System.currentTimeMillis() }?.size

    Scaffold() { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            // A task was pressed.
            if(task != null){
                DisplayTaskDetails(mainViewModel, task) {
                    selectedTask.value = null
                }
            }else {
                Text(
                    text = "Home",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant,
                    thickness = 1.dp
                )

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Welcome Back!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = currentSemester?.name ?: "No Existing Semester",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    SimpleBox(
                        title = "Total Tasks",
                        value = totalTasks.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    SimpleBox(
                        title = "Days Left",
                        value = remainingDays.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    SimpleBox(
                        title = "Missed Tasks",
                        value = missedTasks.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    SimpleBox(
                        title = "Subjects",
                        value = totalSubjects.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                QuoteBox("With every difficulty comes ease")

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Next Deadline",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                val allTasks = currentSemester?.subjects?.flatMap { it.tasks }
                val nextTask = allTasks?.filter{ it.dueDate > System.currentTimeMillis() }?.minByOrNull { it.dueDate }

                if (nextTask != null) {
                    DisplayTask(mainViewModel, nextTask) { pressedTask ->
                        selectedTask.value = pressedTask
                    }
                } else {
                    Column(modifier = Modifier.fillMaxWidth().fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "All caught up! No upcoming tasks.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// Creates the quote box.
@Composable
fun QuoteBox(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth().height(80.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "\"$text\"", modifier = Modifier.padding(16.dp), textAlign = TextAlign.Center )
        }
    }
}

// Creates a simple box for the dashboard information.
@Composable
fun SimpleBox(title: String, value: String, modifier: Modifier ) {
    Card(
        modifier = modifier.height(100.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.labelLarge)
            Text(text = value, style = MaterialTheme.typography.headlineMedium)
        }
    }
}