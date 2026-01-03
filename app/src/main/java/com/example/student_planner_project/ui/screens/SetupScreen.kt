// THE USER INTERFACE FOR SETUP SCREEN

@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.student_planner_project.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.student_planner_project.ui.viewmodels.MainViewModel
import java.util.Calendar

@Composable
fun SetupScreen(mainViewModel: MainViewModel){
    val semesterName = remember { mutableStateOf("") }
    val startDate = remember { mutableStateOf("Select Start Date") }
    val endDate = remember { mutableStateOf("Select End Date") }
    val startTimestamp = remember { mutableLongStateOf(0L) }
    val endTimestamp = remember { mutableLongStateOf(0L) }
    val context = LocalContext.current

    // Organizes the text in the screen vertically.
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome to the Student Planner Application!",style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = semesterName.value,
            onValueChange = {semesterName.value = it},
            label = { Text("Enter Semester Name")},
            modifier = Modifier.fillMaxWidth()
        )

        Text("Semester Dates", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        // START DATE SECTION
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Start Date:", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)

            Button(
                onClick = {
                    openCalendar(context) { timestamp, dateString ->
                        startTimestamp.longValue = timestamp
                        startDate.value = dateString
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.filledTonalButtonColors()
            ) {
                // Displays the selected date in the button
                Text(startDate.value)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // END DATE SECTION
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("End Date:", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)

            Button(
                onClick = {
                    openCalendar(context) { timestamp, dateString ->
                        endTimestamp.longValue = timestamp
                        endDate.value = dateString
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.filledTonalButtonColors()
            ) {
                // Displays the selected date in the button
                Text(endDate.value)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            mainViewModel.saveSemester(semesterName.value, startTimestamp.longValue, endTimestamp.longValue)
            mainViewModel.finishSetup()},
            enabled = semesterName.value.isNotEmpty() && startDate.value != "Select Start Date" && endDate.value != "Select End Date") {
            Text("Save")
        }
    }
}

// Opens the calendar
fun openCalendar(context: Context, onDatePicked: (Long, String) -> Unit) {
    val calendar = Calendar.getInstance()

    android.app.DatePickerDialog(
        context,
        { _, year, month, day ->
            val picked = Calendar.getInstance()
            picked.set(year, month, day)

            onDatePicked(picked.timeInMillis, "$day/${month + 1}/$year")
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}