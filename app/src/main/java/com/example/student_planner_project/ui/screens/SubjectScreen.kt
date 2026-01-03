// THE USER INTERFACE FOR SUBJECT SCREEN

package com.example.student_planner_project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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

    Scaffold(
        // Add Button
        floatingActionButton =  {
            if(pressedAdd.value == false && currentSemester != null) {
                FloatingActionButton(onClick = { pressedAdd.value = true }){
                    Text(text = "+")
                }
            }
        }
    ){ paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)){
            Text(text = "Subjects", style = MaterialTheme.typography.headlineLarge,fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().padding(bottom = 15.dp))

            Box(modifier = Modifier.weight(1f)) {
                // Add a subject
                if (pressedAdd.value == true) {
                    NewSubject(mainViewModel, pressedAdd)

                } else {

                    // No semester
                    if (currentSemester == null) {
                        Text(text = "Create a semester to add subjects.", modifier = Modifier.align(Alignment.Center))

                        // No existing subjects
                    } else if (currentSemester.subjects.isEmpty()) {
                        Text(text = "No existing subjects.", modifier = Modifier.align(Alignment.Center))

                        // Has semester and subjects
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(currentSemester.subjects) { subject ->
                                DisplaySubject(mainViewModel, subject)
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
fun NewSubject(mainViewModel: MainViewModel, pressedAdd: MutableState<Boolean>){
    val subjectName = remember { mutableStateOf("") }
    val subjectProfessor = remember { mutableStateOf("")}
    val subjectSchedule = remember { mutableStateOf("")}

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

// Displays the subject details
@Composable
fun DisplaySubject(mainViewModel: MainViewModel, subject: Subject){
    Card(
        modifier = Modifier.fillMaxWidth()
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
