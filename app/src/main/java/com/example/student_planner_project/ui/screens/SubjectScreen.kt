// THE USER INTERFACE FOR SUBJECT SCREEN

package com.example.student_planner_project.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.student_planner_project.data.models.Subject
import com.example.student_planner_project.ui.viewmodels.MainViewModel

@Composable
fun SubjectScreen(mainViewModel: MainViewModel){
    val semester = mainViewModel.semester.collectAsState()
    val currentSemester = semester.value
    val pressedAdd = remember {mutableStateOf(false)}
    val selectedSubject = remember {mutableStateOf<Subject?>(null)}
    val subject = selectedSubject.value


    Scaffold(
        // Add Button
        floatingActionButton =  {
            if(pressedAdd.value == false && currentSemester != null && subject == null) {
                FloatingActionButton(onClick = { pressedAdd.value = true }){
                    Text(text = "+")
                }
            }
        }
    ){ paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)){
            Box(modifier = Modifier.weight(1f)) {
                // Display the details of the selected subject
                if(subject != null){
                    DisplayDetails(subject){
                        selectedSubject.value = null
                    }

                // Add a subject
                } else if (pressedAdd.value == true) {
                    NewSubject(mainViewModel, pressedAdd){
                        pressedAdd.value = false
                    }

                } else {
                    Column {
                        Text(
                            text = "Subjects",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 15.dp)
                        )

                        // No semester
                        if (currentSemester == null) {
                            Text(
                                text = "Create a semester to add subjects.",
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )

                            // No existing subjects
                        } else if (currentSemester.subjects.isEmpty()) {
                            Text(
                                text = "No existing subjects.",
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )

                            // Has semester and subjects
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(currentSemester.subjects) { subject ->
                                    DisplaySubject(
                                        mainViewModel,
                                        subject,
                                        pressed = { clickedSubject ->
                                            selectedSubject.value = clickedSubject
                                        })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



// Adds a subject
@Composable
fun NewSubject(mainViewModel: MainViewModel, pressedAdd: MutableState<Boolean>, pressedBack: () -> Unit){
    val subjectName = remember { mutableStateOf("") }
    val subjectProfessor = remember { mutableStateOf("")}
    val subjectSchedule = remember { mutableStateOf("")}

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
        Text(text = "ADD NEW SUBJECT", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(text = "Fill up the following fields to add a new subject.")

        // Text Field for Subject Name
        OutlinedTextField(
            value = subjectName.value ,
            onValueChange = {subjectName.value = it},
            label = {Text(text = "Subject Name")},
            modifier = Modifier.fillMaxWidth()
        )

        // Text Field for Professor Name
        OutlinedTextField(
            value = subjectProfessor.value ,
            onValueChange = {subjectProfessor.value = it},
            label = {Text(text = "Professor Name")},
            modifier = Modifier.fillMaxWidth()
        )

        // Text Field for Schedule
        OutlinedTextField(
            value = subjectSchedule.value ,
            onValueChange = {subjectSchedule.value = it},
            label = {Text(text = "Schedule")},
            placeholder = {Text(text = "e.g. Mon/Wed 01:00 PM - 04:00 PM")},
            modifier = Modifier.fillMaxWidth()
        )

        // Save Button
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick =  {
                mainViewModel.addSubject(subjectName.value, subjectProfessor.value, subjectSchedule.value)
                pressedAdd.value = false}
        ){
            Text(text = "Save")
        }
    }
}

// Displays the subject
@Composable
fun DisplaySubject(mainViewModel: MainViewModel, subject: Subject, pressed: (Subject) -> Unit){
    Card(
        modifier = Modifier.fillMaxWidth().clickable{pressed(subject)}
    ){
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = subject.name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(text = subject.professor, style = MaterialTheme.typography.bodyMedium)
                Text(text = subject.schedule, style = MaterialTheme.typography.bodyMedium)
            }

            // Button to delete a subject
            IconButton(
                onClick = {mainViewModel.deleteSubject(subject)}
                ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Subject"
                )
            }
        }
    }
}

// Displays the details of the selected subject
@Composable
fun DisplayDetails(subject: Subject, pressedBack: () -> Unit){
    val selectedTab = remember {mutableStateOf(0)}
    val tabs = listOf("Tasks", "Notes")

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
                text = "Subjects",
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

        Text(text = subject.name, style = MaterialTheme.typography.headlineLarge,fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth())
        Text(text = subject.professor, style = MaterialTheme.typography.bodyMedium)
        Text(text = subject.schedule, style = MaterialTheme.typography.bodyMedium)

        TabRow(
            selectedTabIndex = selectedTab.value,
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab.value == index,
                    text = {Text(text = title)},
                    onClick = {selectedTab.value = index}
                )
            }
        }

        Box(){
            if(selectedTab.value == 0){
                Text(text = "Tasks")
            }else{
                Text(text = "Notes")
            }
        }
    }
}