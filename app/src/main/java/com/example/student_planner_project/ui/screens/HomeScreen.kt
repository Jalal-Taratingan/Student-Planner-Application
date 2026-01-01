// THE USER INTERFACE OF THE HOME SCREEN

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.student_planner_project.ui.viewmodels.MainViewModel

@Composable
fun HomeScreen(mainViewModel : MainViewModel){

    // Makes the display of screen reactive/dynamic for changes.
    val currentSemester = mainViewModel.semester.collectAsState()
    val semester = currentSemester.value

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // Header
        Text(text = "Dashboard", style = MaterialTheme.typography.headlineLarge)
        Text(text = "Semester: ${semester?.name ?: "None"}")

        Spacer(modifier = Modifier.height(20.dp))

        // First Row
        Row(modifier = Modifier.fillMaxWidth()) {
            SimpleBox(title = "Tasks", value = "0", modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            SimpleBox(title = "Remaining Days", value = "82", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Second Row
        Row(modifier = Modifier.fillMaxWidth()){
            SimpleBox(title = "Missed Tasks", value = "0", modifier = Modifier.weight(1f) )
            Spacer(modifier = Modifier.width(8.dp))
            SimpleBox(title = "Events", value = "0", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(8.dp))

        QuoteBox("Don't stop hanggang hindi ka pa masarap.")
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
            Text(text = "\"$text\"", modifier = Modifier.padding(16.dp))
        }
    }
}

// Creates a simple box for the dashboard information.
@Composable
fun SimpleBox(title: String, value: String, modifier: Modifier) {
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