// THE USER INTERFACE FOR TASK SCREEN

package com.example.student_planner_project.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import java.util.Calendar
import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.student_planner_project.data.models.Subject
import com.example.student_planner_project.ui.viewmodels.MainViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import androidx.compose.runtime.derivedStateOf
import com.example.student_planner_project.data.models.Task

@Composable
fun TaskScreen(mainViewModel : MainViewModel) {
    val semester = mainViewModel.semester.collectAsState()
    val currentSemester = semester.value
    val pressedAdd = remember { mutableStateOf(false) }
    val selectedTask = remember { mutableStateOf<Task?>(null) }
    val task = selectedTask.value


    Scaffold(
        floatingActionButton = {
            if (pressedAdd.value == false && task == null && currentSemester?.subjects?.isNotEmpty() == true) {
                FloatingActionButton(onClick = { pressedAdd.value = true }) {
                    Text(text = "+")
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            // Add a task
            if (pressedAdd.value == true) {
                AddTask(mainViewModel, pressedAdd) {
                    pressedAdd.value = false
                }

            // Displays the details of the selected task.
            } else if (task != null) {
                DisplayTaskDetails(mainViewModel,task) {
                    selectedTask.value = null
                }
            } else {
                Column {
                    Text(
                        text = "Tasks",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outlineVariant,
                        thickness = 1.dp
                    )

                    // Has an existing semester
                    if (currentSemester != null) {

                        // Has an existing subjects
                        if (currentSemester.subjects.isNotEmpty()) {
                            val hasTasks = currentSemester.subjects.any { subject ->
                                subject.tasks.isNotEmpty()
                            }

                            // Has an existing tasks
                            if (hasTasks == true) {
                                val allTasks = currentSemester.subjects.flatMap { it.tasks }

                                // Displays the task of all subjects
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    items(allTasks) { task ->
                                        DisplayTask(mainViewModel, task, pressed = { pressedTask ->
                                                selectedTask.value = pressedTask
                                            }
                                        )
                                    }
                                }
                            // No existing tasks
                            } else {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "No existing tasks.")
                                }
                            }
                        // No existing subjects
                        } else {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "Create a subject to add tasks.")
                            }
                        }

                    // No existing semester
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Create a semester and subject to add tasks.")
                        }
                    }
                }
            }
        }
    }
}


// Adds a task
@Composable
fun AddTask(mainViewModel: MainViewModel, pressedAdd: MutableState<Boolean>, pressedBack: () -> Unit){
    val semester = mainViewModel.semester.collectAsState()
    val currentSemester = semester.value

    val taskName = remember { mutableStateOf("") }
    val notes = remember {mutableStateOf("")}
    val pressedChoices = remember { mutableStateOf(false)}
    val selectedSubject = remember {mutableStateOf<Subject?>(null)}

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dueDateLong = remember { mutableStateOf<Long?>(null) }      // Raw Date in milliseconds
    val dueDateString = remember {                                         // String Date
        derivedStateOf {
            val dateLong = dueDateLong.value

            if(dateLong == null){
                "Select Due Date"
            }else {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                dateFormat.format(Date(dateLong))
            }
        }
    }

    // Calendar Dialog
    val datePickerDialog =
        DatePickerDialog(
            context,
            {_, year, month, day ->
                val date = Calendar.getInstance()
                date.set(year, month, day)
                dueDateLong.value = date.timeInMillis
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

    // Back Button
    Box(modifier = Modifier.fillMaxWidth()) {
        IconButton(
            onClick = { pressedBack() },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIos,
                contentDescription = "Back",
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
    ){
        Text(text = "ADD NEW TASK", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(text = "Fill up the following fields to add a new task.")

        Box(modifier = Modifier.fillMaxWidth()){
            // Field for selecting a subject
            OutlinedTextField(
                value = selectedSubject.value?.name?: "Select Subject",
                onValueChange = {},
                readOnly = true,
                label = {Text(text = "Subject")},
                trailingIcon = {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Drop Down Button")
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Drop down menu for subjects
            DropdownMenu(
                expanded = pressedChoices.value,
                onDismissRequest = {pressedChoices.value = false},
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                currentSemester?.subjects?.forEach { subject ->
                    DropdownMenuItem(
                        text ={Text(text = subject.name)},
                        onClick = {
                            selectedSubject.value = subject
                            pressedChoices.value = false}
                    )
                }
            }

            Box(modifier = Modifier.matchParentSize().clickable{pressedChoices.value = true})
        }

        // Text Field for Task Name
        OutlinedTextField(
            value = taskName.value,
            onValueChange = {taskName.value = it},
            label = {Text(text = "Task Name")},
            modifier = Modifier.fillMaxWidth()
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            // Text Field for Schedule
            OutlinedTextField(
                value = dueDateString.value,
                onValueChange = {},
                label = { Text(text = "Due Date") },
                readOnly = true,
                trailingIcon = {
                    Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Calendar Icon")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Box(modifier = Modifier.matchParentSize().clickable{datePickerDialog.show()})
        }

        // Text Field for Notes
        OutlinedTextField(
            value = notes.value,
            onValueChange = {notes.value = it},
            label = {Text(text = "Details")},
            modifier = Modifier.fillMaxWidth().heightIn(min = 150.dp),
            singleLine = false,
            maxLines = 5
        )

        // Save Button
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick =  {
                val subject = selectedSubject.value
                val date = dueDateLong.value

                if (subject != null && date != null && taskName.value != "") {
                    mainViewModel.addTask(subject = subject, taskName.value, date, notes.value)
                    pressedAdd.value = false
                }
            }
        ){
            Text(text = "Save")
        }
    }
}

// Displays the tasks
@Composable
fun DisplayTask(mainViewModel: MainViewModel, task: Task, pressed: (Task) -> Unit){
    Card(
        modifier = Modifier.fillMaxWidth().clickable {pressed(task)}
    ){
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Subject: " + task.subject.name, style = MaterialTheme.typography.bodyMedium)
                Text(text = "Due Date: " + LongToString(task.dueDate), style = MaterialTheme.typography.bodyMedium)
            }

            // Button to delete a task
            IconButton(
                onClick = {mainViewModel.deleteTask(task)}
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Task"
                )
            }
        }
    }
}

// Displays the details of the selected task
@Composable
fun DisplayTaskDetails(mainViewModel: MainViewModel, task: Task, pressedBack: () -> Unit){
    val scrollState = rememberScrollState()

    Column(){
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = { pressedBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIos,
                    contentDescription = "Back",
                )
            }

            Text(
                text = "Tasks",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 1.dp
        )

        Text(text = task.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(5.dp))

        Text(text = "Subject: " + task.subject.name, style = MaterialTheme.typography.bodyLarge)
        Text(text = "Due Date: " + LongToString(task.dueDate), style = MaterialTheme.typography.bodyLarge)

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 1.dp
        )

        Text(text = "Notes and Instructions:", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            Column(modifier = Modifier.padding(16.dp).verticalScroll(scrollState)) {
                Text(text = task.note)
            }
        }

        // Button to delete the task
        Button(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            onClick = {
                mainViewModel.deleteTask(task)
                pressedBack()
            }
        ) {
            Text("Mark as Completed")
        }
    }
}

// Converts the date to a string
fun LongToString(date: Long): String{
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(Date(date))
}

