// THE USER INTERFACE FOR SUBJECT SCREEN

package com.example.student_planner_project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Room
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.filled.Subject
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.student_planner_project.data.models.Notes
import com.example.student_planner_project.data.models.Subject
import com.example.student_planner_project.data.models.Task
import com.example.student_planner_project.ui.theme.darkBlue
import com.example.student_planner_project.ui.theme.darkBlue2
import com.example.student_planner_project.ui.theme.darkGreen
import com.example.student_planner_project.ui.theme.darkOrange2
import com.example.student_planner_project.ui.theme.darkRed
import com.example.student_planner_project.ui.theme.lightBlue
import com.example.student_planner_project.ui.theme.lightGreen
import com.example.student_planner_project.ui.theme.lightOrange
import com.example.student_planner_project.ui.viewmodels.MainViewModel

@Composable
fun SubjectScreen(mainViewModel: MainViewModel){
    val semester = mainViewModel.semester.collectAsState()
    val currentSemester = semester.value
    val pressedAdd = remember {mutableStateOf(false)}
    val selectedSubject = remember {mutableStateOf<Subject?>(null)}
    val subject = selectedSubject.value

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Display the details of the selected subject
        if (subject != null) {
            DisplaySubjectDetails(mainViewModel, subject) {
                selectedSubject.value = null
            }

            // Add a subject
        } else if (pressedAdd.value == true) {
            AddSubject(mainViewModel, pressedAdd) {
                pressedAdd.value = false
            }

        } else {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(color = darkBlue, shape = RoundedCornerShape(bottomStart = 100.dp, bottomEnd = 100.dp)))

            Column(modifier = Modifier.fillMaxSize().padding(start = 36.dp, end = 36.dp, top = 36.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(7.dp)) {
                            Icon(
                                imageVector = Icons.Default.Layers,
                                contentDescription = null,
                                tint = darkBlue,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Subjects",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Has an existing semester
                if (currentSemester != null) {

                    // Has an existing subjects
                    if (currentSemester.subjects.isNotEmpty()) {
                        val subjects = currentSemester.subjects.sortedBy{it.name}

                        LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp), contentPadding = PaddingValues(bottom = 20.dp)) {
                            items(subjects) { subject ->
                                DisplaySubject(
                                    mainViewModel,
                                    subject,
                                    pressed = { pressedSubject ->
                                        selectedSubject.value = pressedSubject
                                    })
                            }
                        }

                    // No existing subjects
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "No existing subjects.")
                        }
                    }

                // No existing semester
                } else {
                    Text(
                        text = "Create a semester to add subjects.",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

        if(pressedAdd.value == false && currentSemester != null && subject == null) {
            FloatingActionButton(containerColor = darkBlue, shape = RoundedCornerShape(30.dp), onClick = { pressedAdd.value = true }, modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)){
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Icon", tint = Color.White)
            }
        }
    }
}

// Adds a subject
@Composable
fun AddSubject(mainViewModel: MainViewModel, pressedAdd: MutableState<Boolean>, pressedBack: () -> Unit){
    val subjectName = remember { mutableStateOf("") }
    val subjectProfessor = remember { mutableStateOf("")}
    val subjectUnits = remember { mutableStateOf("")}
    val subjectSchedule = remember { mutableStateOf("")}
    val subjectRoom = remember {mutableStateOf("")}

    Column(modifier = Modifier.padding(16.dp)) {
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
                    "Subject",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    color = darkBlue
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Fill up the following fields to add a new subject.",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Text Field for Subject Name
            OutlinedTextField(
                value = subjectName.value,
                onValueChange = { subjectName.value = it },
                label = { Text(text = "Subject Name") },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Subject Icon")
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Text Field for Professor Name
            OutlinedTextField(
                value = subjectProfessor.value,
                onValueChange = { subjectProfessor.value = it },
                label = { Text(text = "Professor Name") },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.PersonOutline,
                        contentDescription = "Professor Icon"
                    )
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Text Field for Units
            OutlinedTextField(
                value = subjectUnits.value,
                onValueChange = { subjectUnits.value = it },
                label = { Text(text = "Units") },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.FormatListNumbered,
                        contentDescription = "Units Icon"
                    )
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Text Field for Schedule
            OutlinedTextField(
                value = subjectSchedule.value,
                onValueChange = { subjectSchedule.value = it },
                label = { Text(text = "Schedule") },
                shape = RoundedCornerShape(20.dp),
                placeholder = { Text(text = "e.g. Mon/Wed 01:00 PM - 04:00 PM") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Schedule, contentDescription = "Schedule Icon")
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Text Field for Room
            OutlinedTextField(
                value = subjectRoom.value,
                onValueChange = { subjectRoom.value = it },
                label = { Text(text = "Room Assignment") },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Room, contentDescription = "Room Icon")
                }
            )

            Spacer(modifier = Modifier.height(110.dp))

            // Add Subject Button
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = darkBlue),
                shape = RoundedCornerShape(15.dp),
                onClick = {
                    if (subjectName.value != "" && subjectProfessor.value != "" && subjectSchedule.value != "") {
                        val units = subjectUnits.value.toIntOrNull() ?: 0

                        mainViewModel.addSubject(
                            subjectName.value,
                            units,
                            subjectProfessor.value,
                            subjectSchedule.value,
                            subjectRoom.value
                        )
                        pressedAdd.value = false
                    }
                }
            ) {
                Text(text = "Add Subject", fontSize = 15.sp)
            }
        }
    }
}

// Displays the subject
@Composable
fun DisplaySubject(mainViewModel: MainViewModel, subject: Subject, pressed: (Subject) -> Unit){
    Card(
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth().clickable{pressed(subject)}
    ){
        Row(modifier = Modifier.padding(15.dp), verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = subject.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(4.dp))
                Text(text = subject.schedule, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(12.dp))

                Row{
                    Card(colors = CardDefaults.cardColors(containerColor = lightBlue, contentColor = darkBlue2)){
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(6.dp)){
                            Icon(imageVector = Icons.Default.StickyNote2, contentDescription = "Notes Icon", modifier = Modifier.size(20.dp))

                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = subject.notes.size.toString() + " Notes", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    if(subject.tasks.isNotEmpty()){
                        Card(colors = CardDefaults.cardColors(containerColor = lightOrange, contentColor = darkOrange2)){
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(6.dp)){
                                Icon(imageVector = Icons.Default.TaskAlt, contentDescription = "Task Icon", modifier = Modifier.size(20.dp))

                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = subject.tasks.size.toString() + " Tasks", fontWeight = FontWeight.Bold)
                            }
                        }
                    }else{
                        Card(colors = CardDefaults.cardColors(containerColor = lightGreen, contentColor = darkGreen)){
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(6.dp)){
                                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Done Icon", modifier = Modifier.size(20.dp))

                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "No Tasks", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                }
            }

            // Button to delete a subject
            IconButton(
                onClick = {mainViewModel.deleteSubject(subject)}
                ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete Subject",
                    tint = Color.Red
                )
            }
        }
    }
}

// Displays the details of the selected subject
@Composable
fun DisplaySubjectDetails(mainViewModel: MainViewModel, selectedSubject: Subject, pressedBack: () -> Unit){
    val semesterState = mainViewModel.semester.collectAsState()
    val currentSubject = semesterState.value?.subjects?.find { it.id == selectedSubject.id } ?: selectedSubject
    val selectedTask = remember {mutableStateOf<Task?>(null)}
    val selectedNote = remember {mutableStateOf<Notes?>(null)}
    val notes = selectedNote.value
    val task = selectedTask.value
    val selectedTab = remember {mutableStateOf(0)}
    val tabs = listOf("Tasks", "Notes")


    // Displays the details of the selected task.
    if (task != null) {
        DisplayTaskDetails(mainViewModel, task) {
            selectedTask.value = null
        }

    // Displays the details of the selected note.
    } else if (notes != null) {
        DisplayNotesDetails(mainViewModel, notes) {
            selectedNote.value = null
        }

    // Displays both task and notes.
    } else {
        Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(color = darkBlue, shape = RoundedCornerShape(bottomStart = 100.dp, bottomEnd = 100.dp)))

        Column(modifier = Modifier.padding(36.dp)) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = { pressedBack() }) {
                    Icon(imageVector = Icons.Default.ArrowBackIos, contentDescription = "Back", tint = Color.White)
                }

                Text(
                    text = "Subjects",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                shape = RoundedCornerShape(25.dp),
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = currentSubject.name,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = darkBlue
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Person Icon",
                            tint = darkBlue2
                        )

                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = currentSubject.professor,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Person Icon",
                            tint = darkRed
                        )

                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = currentSubject.schedule,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tabs for Tasks and Notes of the subject.
            TabRow(
                modifier = Modifier.clip(RoundedCornerShape(25.dp)),
                selectedTabIndex = selectedTab.value,
                containerColor = lightBlue,
                divider = {},
                indicator = { tabPosition ->
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPosition[selectedTab.value])
                            .fillMaxHeight()
                            .zIndex(-1f)
                            .background(darkBlue, RoundedCornerShape(25.dp))
                    )

                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab.value == index,
                        onClick = { selectedTab.value = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                color =
                                    if (selectedTab.value == index) {
                                        Color.White
                                    } else {
                                        Color.Gray
                                    }
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(modifier = Modifier.weight(1f)) {
                // Displays the tasks when task tab is pressed.
                if (selectedTab.value == 0) {
                    if (selectedSubject.tasks.isNotEmpty()) {
                        val tasks = currentSubject.tasks

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(tasks) { task ->
                                DisplayTask(task, pressed = { pressedTask ->
                                    selectedTask.value = pressedTask
                                })
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "No tasks assigned yet.")
                        }
                    }
                    // Displays the notes when notes tab is pressed.
                } else {
                    if (selectedSubject.notes.isNotEmpty()) {
                        val notes = currentSubject.notes

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(notes) { note ->
                                DisplayNotes(note, pressed = { pressedNote ->
                                    selectedNote.value = pressedNote
                                })
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "No notes created yet.")
                        }
                    }
                }
            }
        }
    }
}

// Display the task of the selected subject
@Composable
fun DisplayTask(task: Task, pressed: (Task) -> Unit){
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().clickable{pressed(task)},
        shape = RoundedCornerShape(20.dp),
    ){
        Column(modifier = Modifier.padding(8.dp)) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically)
            {
                Text(text = task.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.weight(1f))

                Icon(imageVector = Icons.Default.ArrowForwardIos, contentDescription = "Arrow Forward Icon", tint = Color.Gray, modifier = Modifier.size(18.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.align(Alignment.End), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = LongToString(task.dueDate), color = Color.Gray, fontSize = 15.sp, fontWeight = FontWeight.Normal)
            }
        }
    }
}

// Display the notes of the selected subject
@Composable
fun DisplayNotes(notes: Notes, pressed: (Notes) -> Unit){
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().clickable {pressed(notes)},
        shape = RoundedCornerShape(20.dp),
    ){
        Column(modifier = Modifier.padding(8.dp)) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(text = notes.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.weight(1f))

                Icon(imageVector = Icons.Default.ArrowForwardIos, contentDescription = "Arrow Forward Icon", tint = Color.Gray, modifier = Modifier.size(18.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.align(Alignment.End), verticalAlignment = Alignment.CenterVertically) {
                Text(text = LongToString(notes.dateCreated), color = Color.Gray, fontSize = 15.sp, fontWeight = FontWeight.Normal)
            }
        }
    }
}