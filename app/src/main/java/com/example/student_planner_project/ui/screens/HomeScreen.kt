// THE USER INTERFACE OF HOME SCREEN

package com.example.student_planner_project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.student_planner_project.data.models.Notes
import com.example.student_planner_project.data.models.Subject
import com.example.student_planner_project.data.models.Task
import com.example.student_planner_project.ui.navigation.Screen
import com.example.student_planner_project.ui.theme.darkBlue
import com.example.student_planner_project.ui.theme.darkGreen
import com.example.student_planner_project.ui.theme.darkOrange
import com.example.student_planner_project.ui.theme.darkRed
import com.example.student_planner_project.ui.theme.lightBlue
import com.example.student_planner_project.ui.theme.lightGreen
import com.example.student_planner_project.ui.theme.lightOrange
import com.example.student_planner_project.ui.theme.lightRed
import com.example.student_planner_project.ui.viewmodels.MainViewModel

@Composable
fun HomeScreen(mainViewModel: MainViewModel, navController: NavHostController) {
    val semester = mainViewModel.semester.collectAsState()
    val currentSemester = semester.value
    val totalTasks = currentSemester?.subjects?.sumOf {it.tasks.size} ?: 0
    val totalSubjects = semester.value?.subjects?.size ?: 0
    val totalUnits = currentSemester?.subjects?.sumOf {it.units} ?: 0
    val selectedTask = remember {mutableStateOf<Task?>(null)}
    val selectedNotes = remember {mutableStateOf<Notes?>(null)}
    val selectedSubject = remember {mutableStateOf<Subject?>(null)}
    val pressedSettings = remember {mutableStateOf(false)}
    val task = selectedTask.value
    val notes = selectedNotes.value
    val subject = selectedSubject.value
    val scrollState = rememberScrollState()
    val remainingDays =
        if(currentSemester != null){
            val time = currentSemester.endDate - System.currentTimeMillis()
            val days = TimeUnit.MILLISECONDS.toDays(time)

            maxOf(0, days)
        }else{
            0
        }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // A task was pressed.
        if (task != null) {
            DisplayTaskDetails(mainViewModel, task) {
                selectedTask.value = null
            }

            // A note was pressed.
        } else if (notes != null) {
            DisplayNotesDetails(mainViewModel, notes) {
                selectedNotes.value = null
            }

            // A subject was pressed.
        } else if (subject != null) {
            DisplaySubjectDetails(mainViewModel, subject) {
                selectedSubject.value = null
            }
        } else if (pressedSettings.value != false) {
            displaySettings(mainViewModel) { pressedSettings.value = false }

        } else {
            Column(modifier = Modifier.verticalScroll(scrollState)){
                Box(modifier = Modifier.fillMaxWidth()){
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(color = darkBlue, shape = RoundedCornerShape(bottomStart = 100.dp, bottomEnd = 100.dp)))

                    Column(modifier = Modifier.padding(36.dp)){
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Row {
                                Card(
                                    shape = RoundedCornerShape(15.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                                ) {
                                    Column(modifier = Modifier.padding(7.dp)) {
                                        Icon(
                                            imageVector = Icons.Default.Dashboard,
                                            contentDescription = null,
                                            tint = darkBlue,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = "Home",
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            IconButton(onClick = {pressedSettings.value = true}) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "settings",
                                    tint = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = currentSemester?.name ?: "No Existing Semester",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        Column(modifier = Modifier.fillMaxSize()) {
                            val allTasks = currentSemester?.subjects?.flatMap { it.tasks }
                            val nextTask = allTasks?.filter { !it.isCompleted }?.minByOrNull { it.dueDate }

                            Row() {
                                Card(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(25.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEBF5FF)),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(20.dp),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Card(
                                            shape = RoundedCornerShape(20.dp),
                                            colors = CardDefaults.cardColors(containerColor = Color.White),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                                        ) {
                                            Column(modifier = Modifier.padding(8.dp)) {
                                                Icon(
                                                    imageVector = Icons.Default.Task,
                                                    contentDescription = null,
                                                    tint = Color(0xFF3B82F6),
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(
                                            text = "ACTIVE TASKS",
                                            color = Color(0xFF3B82F6),
                                            fontSize = 14.sp,
                                            letterSpacing = 0.5.sp
                                        )
                                        Spacer(modifier = Modifier.height(5.dp))
                                        Text(
                                            text = totalTasks.toString() + " Pending",
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(20.dp))

                                Card(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(25.dp),
                                    colors = CardDefaults.cardColors(containerColor = lightGreen),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(20.dp),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Card(
                                            shape = RoundedCornerShape(20.dp),
                                            colors = CardDefaults.cardColors(containerColor = Color.White),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                                        ) {
                                            Column(modifier = Modifier.padding(8.dp)) {
                                                Icon(
                                                    imageVector = Icons.Default.CalendarMonth,
                                                    contentDescription = null,
                                                    tint = darkGreen,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(
                                            text = "DAYS LEFT",
                                            color = darkGreen,
                                            fontSize = 14.sp,
                                            letterSpacing = 0.5.sp
                                        )
                                        Spacer(modifier = Modifier.height(5.dp))
                                        Text(
                                            text = remainingDays.toString() + " Days",
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Row() {
                                Card(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(25.dp),
                                    colors = CardDefaults.cardColors(containerColor = lightOrange),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(20.dp),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Card(
                                            shape = RoundedCornerShape(20.dp),
                                            colors = CardDefaults.cardColors(containerColor = Color.White),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                                        ) {
                                            Column(modifier = Modifier.padding(8.dp)) {
                                                Icon(
                                                    imageVector = Icons.Default.School,
                                                    contentDescription = null,
                                                    tint = darkOrange,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(
                                            text = "SUBJECTS",
                                            color = darkOrange,
                                            fontSize = 14.sp,
                                            letterSpacing = 0.5.sp
                                        )
                                        Spacer(modifier = Modifier.height(5.dp))
                                        Text(
                                            text = totalSubjects.toString() +
                                                    if (totalSubjects == 1 or 0) {
                                                        " Subject"
                                                    } else {
                                                        " Subjects"
                                                    },
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(20.dp))

                                Card(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(25.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F3FF)),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(20.dp),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Card(
                                            shape = RoundedCornerShape(20.dp),
                                            colors = CardDefaults.cardColors(containerColor = Color.White),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                                        ) {
                                            Column(modifier = Modifier.padding(8.dp)) {
                                                Icon(
                                                    imageVector = Icons.Default.BarChart,
                                                    contentDescription = null,
                                                    tint = Color(0xFF8B5CF6),
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(
                                            text = "UNITS ENROLLED",
                                            color = Color(0xFF8B5CF6),
                                            fontSize = 14.sp,
                                            letterSpacing = 0.5.sp
                                        )
                                        Spacer(modifier = Modifier.height(5.dp))
                                        Text(
                                            text = totalUnits.toString() +
                                                    if (totalUnits == 1 or 0) {
                                                        " Unit"
                                                    } else {
                                                        " Units"
                                                    },
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(28.dp))

                            Row() {
                                Text(
                                    text = "Your Subjects",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "See All",
                                    color = darkBlue,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable { navController.navigate(Screen.Subjects.route) })
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Row() {
                                val sampleSubjects = currentSemester?.subjects?.take(2) ?: emptyList()

                                if (sampleSubjects.isNotEmpty()) {
                                    sampleSubjects.forEachIndexed { index, subject ->
                                        displaySampleSubjects(
                                            subject,
                                            modifier = Modifier.weight(1f)
                                        ) { subject ->
                                            selectedSubject.value = subject
                                        }

                                        if (index == 0 && sampleSubjects.size > 1) {
                                            Spacer(modifier = Modifier.width(20.dp))
                                        }
                                    }
                                } else {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Spacer(modifier = Modifier.height(20.dp))
                                        Text(
                                            text = "No existing subjects yet.",
                                            color = Color.Gray
                                        )
                                        Spacer(modifier = Modifier.height(20.dp))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(28.dp))

                            Text(
                                text = "Next Deadlines",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            if (nextTask != null) {
                                displayNextTask(nextTask) { task ->
                                    selectedTask.value = task
                                }
                            } else {
                                Column(
                                    modifier = Modifier.fillMaxWidth().fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Text(
                                        text = "All caught up! No upcoming tasks.",
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(28.dp))

                            Row() {
                                Text(
                                    text = "Recent Notes",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "See All",
                                    color = darkBlue,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable { navController.navigate(Screen.Notes.route) })
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Column() {
                                val allNotes = currentSemester?.subjects?.flatMap { it.notes } ?: emptyList()
                                val sampleNotes = allNotes.sortedByDescending { it.dateCreated }.take(3)

                                if (sampleNotes.isNotEmpty()) {
                                    sampleNotes.forEachIndexed { index, notes ->
                                        displaySampleNotes(notes) { notes ->
                                            selectedNotes.value = notes
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))
                                    }
                                } else {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Spacer(modifier = Modifier.height(20.dp))
                                        Text(
                                            text = "No recent notes yet.",
                                            color = Color.Gray
                                        )
                                        Spacer(modifier = Modifier.height(20.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Display a sample of the subjects in the list.
@Composable
fun displaySampleSubjects(sampleSubject: Subject, modifier: Modifier, pressed: (Subject) -> Unit){
    Card(
        shape = RoundedCornerShape(25.dp),
        modifier = modifier.height(200.dp).clickable {pressed(sampleSubject)},
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(18.dp)) {
            Card(shape = RoundedCornerShape(25.dp), colors = CardDefaults.cardColors(containerColor = lightBlue)){
                Column(modifier = Modifier.padding(10.dp)) {
                    Icon(imageVector = Icons.Default.LibraryBooks, contentDescription = null, tint = darkBlue, modifier = Modifier.size(28.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = sampleSubject.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(10.dp))

            // Schedule
            Row(verticalAlignment = Alignment.CenterVertically){
                val schedule = sampleSubject.schedule
                val day = schedule.substring(0, 7)

                Card(colors = CardDefaults.cardColors(containerColor = lightBlue)){
                    Column(modifier = Modifier.padding(4.dp)) {
                        Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = darkBlue, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))
                Text(text = day, color = Color.Gray, fontSize = 15.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Room
            Row(verticalAlignment = Alignment.CenterVertically){
                Card(colors = CardDefaults.cardColors(containerColor = lightRed)){
                    Column(modifier = Modifier.padding(4.dp)) {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = darkRed, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))
                Text(text = sampleSubject.room, color = Color.Gray, fontSize = 15.sp)
            }
        }
    }
}

// Display a sample of the notes in the list.
@Composable
fun displaySampleNotes(sampleNotes: Notes, pressed: (Notes) -> Unit){
    Card(
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth().clickable {pressed(sampleNotes)}
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically){
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = darkBlue)) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Icon(imageVector = Icons.Default.StickyNote2, contentDescription = null, tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(){
                Text(text = sampleNotes.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = LongToString(sampleNotes.dateCreated), color = Color.Gray, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(imageVector = Icons.Default.ArrowForwardIos, contentDescription = "Arrow Forward Icon", tint = Color.Gray, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
fun displayNextTask(nextTask: Task, pressed: (Task) -> Unit){
    Card(
        shape = RoundedCornerShape(25.dp),
        modifier = Modifier.fillMaxWidth().clickable {pressed(nextTask)},
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.padding(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Card(
                        shape = RoundedCornerShape(50.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = lightRed,
                            contentColor = darkRed
                        )
                    ) {
                        Column(modifier = Modifier.padding(4.dp)) {
                            Text(
                                text = "HIGH PRIORITY",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = nextTask.title,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Due: " + LongToString(nextTask.dueDate) + " ~ " + nextTask.subject.name,
                        color = Color.Gray,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(imageVector = Icons.Default.ArrowForwardIos, contentDescription = "Arrow Forward Icon", tint = Color.Gray, modifier = Modifier.size(18.dp))
        }
    }
}

// Display the settings.
@Composable
fun displaySettings(mainViewModel : MainViewModel ,pressedBack: () -> Unit){
    val pressedDelete = remember{mutableStateOf(false)}

    if(pressedDelete.value == true){
        confirmDelete(mainViewModel){
            pressedDelete.value = false
        }

    }else {
        Box(
            modifier = Modifier.fillMaxWidth().height(200.dp).background(
                color = darkBlue,
                shape = RoundedCornerShape(bottomStart = 100.dp, bottomEnd = 100.dp)
            )
        )

        Column(modifier = Modifier.padding(36.dp)) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = { pressedBack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIos,
                        contentDescription = "Back Button",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Data Management",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth().clickable { pressedDelete.value = true },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = lightRed),
                        elevation = CardDefaults.cardElevation(2.dp),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Icon(
                                imageVector = Icons.Default.DeleteForever,
                                contentDescription = "Delete Semester",
                                tint = darkRed,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(18.dp))

                    Column {
                        Text(
                            text = "Delete All Data",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = darkRed
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "This action cannot be undone.",
                            color = Color.Gray,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = lightGreen),
                        elevation = CardDefaults.cardElevation(2.dp),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add New Semester",
                                tint = darkGreen,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(18.dp))

                    Column {
                        Text(
                            text = "Create New Semester",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = darkGreen
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Create a new semester.",
                            color = Color.Gray,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

// Shows a confirmation box for deleting all data.
@Composable
fun confirmDelete(mainViewModel : MainViewModel, pressedBack : () -> Unit){
    Card(modifier = Modifier.fillMaxSize(), colors = CardDefaults.cardColors(containerColor = Color.White)){
        Column(modifier = Modifier.padding(36.dp)){
            Card(
                colors = CardDefaults.cardColors(containerColor = lightRed),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(15.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = "Delete Semester",
                        tint = darkRed,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Text(text = "Delete All Data?", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(5.dp))

            Text(text = "This action cannot be undone. All your subjects, tasks, and notes will be permanently removed.", color = Color.Gray, fontSize = 15.sp, fontWeight = FontWeight.Normal)

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = darkRed),
                shape = RoundedCornerShape(15.dp),
                onClick = {
                    mainViewModel.deleteSemester()
                    pressedBack()}){
                Text(text = "Delete", color = Color.White)
            }

            Spacer(modifier = Modifier.height(5.dp))

            Button(modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray), shape = RoundedCornerShape(15.dp), onClick = {pressedBack()}){
                Text(text = "Cancel")
            }
        }
    }
}