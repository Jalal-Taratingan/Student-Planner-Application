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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.student_planner_project.data.models.Notes
import com.example.student_planner_project.data.models.Subject
import com.example.student_planner_project.ui.theme.darkBlue
import com.example.student_planner_project.ui.theme.darkBlue2
import com.example.student_planner_project.ui.theme.lightBlue
import com.example.student_planner_project.ui.viewmodels.MainViewModel

@Composable
fun NotesScreen(mainViewModel: MainViewModel){
    val semester = mainViewModel.semester.collectAsState()
    val currentSemester = semester.value
    val pressedAdd = remember { mutableStateOf(false) }
    val selectedNote = remember { mutableStateOf<Notes?>(null) }
    val note = selectedNote.value

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Add a note
        if (pressedAdd.value == true) {
            AddNotes(mainViewModel, pressedAdd) {
                pressedAdd.value = false
            }

        // Displays the details of the selected notes.
        } else if (note != null) {
            DisplayNotesDetails(mainViewModel, note) {
                selectedNote.value = null
            }

        } else {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(color = darkBlue, shape = RoundedCornerShape(bottomStart = 100.dp, bottomEnd = 100.dp)))

            Column(modifier = Modifier.fillMaxSize().padding(start = 36.dp, end = 36.dp, top = 36.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Card(shape = RoundedCornerShape(15.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)){
                        Column(modifier = Modifier.padding(7.dp)) {
                            Icon(imageVector = Icons.Default.StickyNote2, contentDescription = null, tint = darkBlue, modifier = Modifier.size(22.dp))
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(text = "Notes", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Has an existing semester
                if (currentSemester != null) {

                    // Has an existing subjects
                    if (currentSemester.subjects.isNotEmpty()) {
                        val hasNotes = currentSemester.subjects.any { subject ->
                            subject.notes.isNotEmpty()
                        }
                        // Has an existing notes
                        if (hasNotes == true) {
                            val allNotes = currentSemester.subjects.flatMap { subject -> subject.notes }.sortedByDescending { it.dateCreated }

                            LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp), contentPadding = PaddingValues(bottom = 20.dp)) {
                                items(allNotes) { notes ->
                                    DisplayNotes(
                                        mainViewModel,
                                        notes,
                                        pressed = { pressedNotes ->
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
                        // No existing subjects
                    } else {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Create a subject to add notes.")
                        }
                    }
                    // No existing semester
                } else {
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Create a semester and subject to add notes.")
                    }
                }
            }
        }

        if (pressedAdd.value == false && note == null && currentSemester?.subjects?.isNotEmpty() == true) {
            FloatingActionButton(containerColor = darkBlue, shape = RoundedCornerShape(30.dp), onClick = { pressedAdd.value = true }, modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Icon", tint = Color.White)
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

    Column(modifier = Modifier.padding(16.dp)) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = { pressedBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIos,
                    contentDescription = "Back Button",
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
                    "Notes",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    color = darkBlue
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Fill up the following fields to add a new notes.",
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

            // Text Field for Notes Name
            OutlinedTextField(
                value = notesName.value,
                onValueChange = { notesName.value = it },
                label = { Text(text = "Notes Name") },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DriveFileRenameOutline,
                        contentDescription = "Notes Name Icon",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )

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
                    if (subject != null && notesName.value != "" && notes.value != "") {
                        mainViewModel.addNotes(subject, notesName.value, notes.value)
                        pressedAdd.value = false
                    }
                }
            ) {
                Text(text = "Add Notes")
            }
        }
    }
}

// Displays the notes
@Composable
fun DisplayNotes(mainViewModel: MainViewModel, notes: Notes, pressed: (Notes) -> Unit){
    Card(
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth().clickable {pressed(notes)}
    ){
        Row(modifier = Modifier.padding(15.dp), verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = notes.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(10.dp))

                // Displays the subject of the task.
                Card(colors = CardDefaults.cardColors(containerColor = lightBlue, contentColor = darkBlue2)){
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(6.dp)){
                        Icon(imageVector = Icons.Default.Layers, contentDescription = "Subject Icon", modifier = Modifier.size(20.dp))

                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = notes.subject.name, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Button to delete a note
            IconButton(onClick = {mainViewModel.deleteNotes(notes)}) {
                Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Delete Notes", tint = Color.Red)
            }
        }
    }
}

// Displays the details of the selected notes
@Composable
fun DisplayNotesDetails(mainViewModel: MainViewModel, notes: Notes, pressedBack: () -> Unit){
    val isEditing = remember {mutableStateOf(false)}
    val currentNotes = remember {mutableStateOf(notes.notes)}
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(color = darkBlue, shape = RoundedCornerShape(bottomStart = 100.dp, bottomEnd = 100.dp)))

    Column(modifier = Modifier.padding(36.dp)) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = { pressedBack() }) {
                Icon(imageVector = Icons.Default.ArrowBackIos, contentDescription = "Back", tint = Color.White)
            }

            Text(
                text = "Notes",
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
                        text = notes.title,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = darkBlue
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Layers,
                            contentDescription = "Subject Icon",
                            tint = darkBlue2
                        )

                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = notes.subject.name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant, thickness = 1.dp)

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Notes",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = darkBlue
                )

                IconButton(onClick = { isEditing.value = !isEditing.value }) {
                    Icon(
                        imageVector =
                            if (isEditing.value != true) {
                                Icons.Default.Edit
                            } else {
                                Icons.Default.Check
                            },
                        contentDescription = "Edit and Save Button",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
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
                        Text(
                            text = currentNotes.value,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (isEditing.value == true) {
                // Save button
                Button(
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = darkBlue),
                    shape = RoundedCornerShape(15.dp),
                    onClick = {
                        val updatedNote = notes.copy(notes = currentNotes.value)
                        mainViewModel.editNotes(updatedNote)

                        isEditing.value = false
                    }
                ) {
                    Text("Save Changes", fontSize = 15.sp)
                }
            }
        }
    }
}