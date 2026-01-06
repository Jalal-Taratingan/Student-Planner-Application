package com.example.student_planner_project.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.student_planner_project.data.models.Notes
import com.example.student_planner_project.data.models.Subject
import com.example.student_planner_project.ui.viewmodels.MainViewModel


@Composable
fun NotesScreen(mainViewModel: MainViewModel){
    val semester = mainViewModel.semester.collectAsState()
    val currentSemester = semester.value
    val pressedAdd = remember { mutableStateOf(false) }
    val selectedNote = remember { mutableStateOf<Notes?>(null) }
    val note = selectedNote.value

    Scaffold(
        floatingActionButton = {
            if (pressedAdd.value == false && currentSemester?.subjects != null && note == null) {
                FloatingActionButton(onClick = { pressedAdd.value = true }) {
                    Text(text = "+")
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            // Add a note
            if(pressedAdd.value == true) {
                AddNotes(mainViewModel, pressedAdd) {
                    pressedAdd.value = false
                }

            // Displays the details of the selected notes.
            }else if(note != null){
                DisplayDetails(mainViewModel, note){
                    selectedNote.value = null
                }

            }else {
                Column {
                    Text(
                        text = "Notes",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 15.dp)
                    )

                    // Has an existing semester
                    if (currentSemester != null) {
                        // Has an existing subjects
                        if (currentSemester.subjects.isNotEmpty()) {
                            val hasNotes = currentSemester.subjects.any { subject ->
                                subject.notes.isNotEmpty()
                            }
                            // Has an existing notes
                            if (hasNotes == true) {
                                val allNotes = currentSemester.subjects.flatMap { subject ->
                                    subject.notes
                                }

                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    items(allNotes) { notes ->
                                        DisplayNotes(mainViewModel, notes, pressed = {pressedNotes ->
                                            selectedNote.value = pressedNotes
                                        })
                                    }
                                }

                            // No existing notes
                            } else {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "No existing notes.")
                                }
                            }
                        // No existing subject
                        } else {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "Create a subject to add notes.")
                            }
                        }

                    // No existing semester
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Create a semester and subject to add notes.")
                        }
                    }
                }
            }
        }
    }
}

// Adds a note
@Composable
fun AddNotes(mainViewModel: MainViewModel, pressedAdd: MutableState<Boolean>, pressedBack: () -> Unit){
    val semester = mainViewModel.semester.collectAsState()
    val currentSemester = semester.value
    val notesName = remember {mutableStateOf("")}
    val pressedChoices = remember { mutableStateOf(false)}
    val selectedSubject = remember {mutableStateOf<Subject?> (null)}
    val notes = remember {mutableStateOf("")}

    Box(modifier = Modifier.fillMaxWidth()){
        IconButton(onClick = {pressedBack()}, modifier = Modifier.align(Alignment.CenterStart)) {
            Icon(
                imageVector = Icons.Default.ArrowBackIos, contentDescription = "Back Button"
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically))
    {
        Text(text = "ADD NEW NOTES", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(text = "Fill up the following fields to add a new notes.")

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

        // Text Field for Notes Name
        OutlinedTextField(
            value = notesName.value,
            onValueChange = {notesName.value = it},
            label = {Text(text = "Notes Name")},
            modifier = Modifier.fillMaxWidth()
        )

        // Text Field for Notes
        OutlinedTextField(
            value = notes.value,
            onValueChange = {notes.value = it},
            label = {Text(text = "Notes")},
            modifier = Modifier.fillMaxWidth().heightIn(min = 150.dp),
            singleLine = false,
            maxLines = 5
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val subject = selectedSubject.value
                if (subject != null) {
                    mainViewModel.addNotes(subject, notesName.value, notes.value)
                    pressedAdd.value = false
                }}
        ){
            Text(text = "Save")
        }
    }
}

// Displays the notes
@Composable
fun DisplayNotes(mainViewModel: MainViewModel, notes: Notes, pressed: (Notes) -> Unit){
    Card(
        modifier = Modifier.fillMaxWidth().clickable {pressed(notes)}
    ){
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notes.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Subject: " + notes.subject.name, style = MaterialTheme.typography.bodyMedium)
            }

            // Button to delete a note
            IconButton(
                onClick = {mainViewModel.deleteNotes(notes)}
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Notes"
                )
            }
        }
    }
}

// Displays the details of the selected notes
@Composable
fun DisplayDetails(mainViewModel: MainViewModel, notes: Notes, pressedBack: () -> Unit){
    val isEditing = remember {mutableStateOf(false)}
    val currentNotes = remember {mutableStateOf(notes.notes)}
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
                text = "Notes",
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

        Text(text = notes.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(5.dp))

        Text(text = "Subject: " + notes.subject.name, style = MaterialTheme.typography.bodyLarge)

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 1.dp
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = "Notes: ", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)

            IconButton(onClick = {isEditing.value = !isEditing.value}) {
                Icon(
                    imageVector =
                        if(isEditing.value != true){
                            Icons.Default.Edit
                        }else{
                            Icons.Default.Check
                        }
                    , contentDescription = "Edit and Save Button")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            Box(modifier = Modifier.padding(16.dp).verticalScroll(scrollState)) {
                // Can edit the text
                if (isEditing.value == true) {
                    BasicTextField(
                        value = currentNotes.value,
                        onValueChange = { currentNotes.value = it },
                        textStyle = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth()
                    )
                // Displays only the text
                } else {
                    Text(text = currentNotes.value, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        if(isEditing.value == true){
            // Save button
            Button(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                onClick = {
                    val updatedNote = notes.copy(notes = currentNotes.value)
                    mainViewModel.editNotes(updatedNote)

                    isEditing.value = false
                }
            ) {
                Text("Save Changes")
            }
        }
    }
}

