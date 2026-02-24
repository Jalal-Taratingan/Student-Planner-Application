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
import androidx.compose.runtime.Composable
import java.util.Calendar
import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.student_planner_project.data.models.Task
import com.example.student_planner_project.ui.theme.darkBlue
import com.example.student_planner_project.ui.theme.darkBlue2
import com.example.student_planner_project.ui.theme.darkGreen
import com.example.student_planner_project.ui.theme.darkOrange
import com.example.student_planner_project.ui.theme.darkOrange2
import com.example.student_planner_project.ui.theme.lightBlue
import com.example.student_planner_project.ui.theme.lightOrange

@Composable
fun TaskScreen(mainViewModel : MainViewModel) {
    val semester = mainViewModel.semester.collectAsState()
    val currentSemester = semester.value
    val pressedAdd = remember { mutableStateOf(false) }
    val selectedTask = remember { mutableStateOf<Task?>(null) }
    val task = selectedTask.value

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Add a task
        if (pressedAdd.value == true) {
            AddTask(mainViewModel, pressedAdd) {
                pressedAdd.value = false
            }

            // Displays the details of the selected task.
        } else if (task != null) {
            DisplayTaskDetails(mainViewModel, task) {
                selectedTask.value = null
            }
        } else {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(color = darkBlue, shape = RoundedCornerShape(bottomStart = 100.dp, bottomEnd = 100.dp)))

            Column(modifier = Modifier.fillMaxSize().padding(start = 36.dp, end = 36.dp, top = 36.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Card(shape = RoundedCornerShape(15.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)){
                        Column(modifier = Modifier.padding(7.dp)) {
                            Icon(imageVector = Icons.Default.Assignment, contentDescription = null, tint = darkBlue, modifier = Modifier.size(22.dp))
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(text = "Tasks", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Has an existing semester
                if (currentSemester != null) {

                    // Has an existing subjects
                    if (currentSemester.subjects.isNotEmpty()) {
                        val hasTasks = currentSemester.subjects.any { subject ->
                            subject.tasks.isNotEmpty()
                        }

                        // Has an existing tasks
                        if (hasTasks == true) {
                            val allTasks = currentSemester.subjects.flatMap { it.tasks }.sortedBy { it.dueDate }

                            // Displays the task of all subjects
                            LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp), contentPadding = PaddingValues(bottom = 20.dp)) {
                                items(allTasks) { task ->
                                    DisplayTask(mainViewModel, task, pressed = { pressedTask ->
                                        selectedTask.value = pressedTask
                                    }
                                    )
                                }
                            }
                            // No existing tasks
                        } else {
                            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "No existing tasks.")
                            }
                        }
                        // No existing subjects
                    } else {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Create a subject to add tasks.")
                        }
                    }
                    // No existing semester
                } else {
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Create a semester and subject to add tasks.")
                    }
                }
            }
        }

        if (pressedAdd.value == false && task == null && currentSemester?.subjects?.isNotEmpty() == true) {
            FloatingActionButton(containerColor = darkBlue, shape = RoundedCornerShape(30.dp), onClick = { pressedAdd.value = true }, modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Icon", tint = Color.White)
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

            if(dateLong != null){
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                dateFormat.format(Date(dateLong))
            }else{
                ""
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

    Column(modifier = Modifier.padding(16.dp)) {
        // Back Button
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = { pressedBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIos,
                    contentDescription = "Back",
                    modifier = Modifier.align(Alignment.TopStart)
                )
            }
        }

        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Row {
                Text(
                    "Add New ",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
                Text(
                    "Task",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    color = darkBlue
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Fill up the following fields to add a new task.",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(25.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                // Field for selecting a subject
                OutlinedTextField(
                    value = selectedSubject.value?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(text = "Subject") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Book,
                            contentDescription = "Subject Icon",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Drop Down Button",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )

                // Drop down menu for subjects
                DropdownMenu(
                    expanded = pressedChoices.value,
                    onDismissRequest = { pressedChoices.value = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    currentSemester?.subjects?.forEach { subject ->
                        DropdownMenuItem(
                            text = { Text(text = subject.name) },
                            onClick = {
                                selectedSubject.value = subject
                                pressedChoices.value = false
                            }
                        )
                    }
                }

                Box(modifier = Modifier.matchParentSize().clickable { pressedChoices.value = true })
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Text Field for Task Name
            OutlinedTextField(
                value = taskName.value,
                onValueChange = { taskName.value = it },
                label = { Text(text = "Task Name") },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Task,
                        contentDescription = "Task Icon",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                // Text Field for Due Date
                OutlinedTextField(
                    value = dueDateString.value,
                    onValueChange = {},
                    label = { Text(text = "Due Date") },
                    shape = RoundedCornerShape(20.dp),
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Calendar Icon",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )

                Box(modifier = Modifier.matchParentSize().clickable { datePickerDialog.show() })
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Text Field for Notes
            OutlinedTextField(
                value = notes.value,
                onValueChange = { notes.value = it },
                label = { Text(text = "Details") },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().height(150.dp),
                singleLine = false,
                maxLines = 5,
                leadingIcon = {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notes,
                            contentDescription = "Notes Icon",
                            modifier = Modifier.padding(top = 16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(70.dp))

            // Save Button
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = darkBlue),
                shape = RoundedCornerShape(15.dp),
                onClick = {
                    val subject = selectedSubject.value
                    val date = dueDateLong.value

                    if (subject != null && date != null && taskName.value != "") {
                        mainViewModel.addTask(subject = subject, taskName.value, date, notes.value)
                        pressedAdd.value = false
                    }
                }
            ) {
                Text(text = "Add Task", fontSize = 15.sp)
            }
        }
    }
}

// Displays the tasks
@Composable
fun DisplayTask(mainViewModel: MainViewModel, task: Task, pressed: (Task) -> Unit){
    Card(
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth().clickable {pressed(task)}
    ){
        Row(modifier = Modifier.padding(15.dp), verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = task.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(10.dp))

                Row{
                    // Displays the subject of the task.
                    Card(colors = CardDefaults.cardColors(containerColor = lightBlue, contentColor = darkBlue2)){
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(6.dp)){
                            Icon(imageVector = Icons.Default.Layers, contentDescription = "Subject Icon", modifier = Modifier.size(20.dp))

                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = task.subject.name, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Displays the due date of the task.
                    Card(colors = CardDefaults.cardColors(containerColor = lightOrange, contentColor = darkOrange2)){
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(6.dp)){
                            Icon(imageVector = Icons.Default.Event, contentDescription = "Due Date Icon", modifier = Modifier.size(20.dp))

                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = LongToString(task.dueDate), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Button to delete a task
            IconButton(onClick = {mainViewModel.deleteTask(task)}) {
                Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Delete Task", tint = Color.Red)
            }
        }
    }
}

// Displays the details of the selected task
@Composable
fun DisplayTaskDetails(mainViewModel: MainViewModel, task: Task, pressedBack: () -> Unit){
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(color = darkBlue, shape = RoundedCornerShape(bottomStart = 100.dp, bottomEnd = 100.dp)))

    Column(modifier = Modifier.padding(36.dp)) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = { pressedBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIos,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = "Tasks",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        Column {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = darkBlue
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Layers,
                            contentDescription = null,
                            tint = darkBlue2
                        )

                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = task.subject.name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = null,
                            tint = darkOrange2
                        )

                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = LongToString(task.dueDate),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant, thickness = 1.dp)

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Notes and Instructions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = darkBlue
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
            ) {
                Column(modifier = Modifier.padding(16.dp).verticalScroll(scrollState)) {
                    Text(text = task.note, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            // Button to delete the task
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = darkBlue),
                shape = RoundedCornerShape(15.dp),
                onClick = {
                    mainViewModel.deleteTask(task)
                    pressedBack()
                }
            ) {
                Text("Mark as Completed")
            }
        }
    }
}

// Converts the date to a string
fun LongToString(date: Long): String{
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(Date(date))
}