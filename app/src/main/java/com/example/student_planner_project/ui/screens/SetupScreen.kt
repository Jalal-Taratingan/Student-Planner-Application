// THE USER INTERFACE FOR SETUP SCREEN

@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.student_planner_project.ui.screens

import android.app.DatePickerDialog
import android.content.Context
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.student_planner_project.ui.viewmodels.MainViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun SetupScreen(mainViewModel: MainViewModel){
    val context = LocalContext.current
    val lightBlue = Color(0xFF3B82F6)
    val semesterName = remember { mutableStateOf("") }
    val startDateLong = remember { mutableStateOf<Long?>(null) }        // Raw Date in milliseconds
    val endDateLong = remember { mutableStateOf<Long?>(null) }          // Raw Date in milliseconds
    val startDateString = remember {                                            // String Date
        derivedStateOf {
            dateInString(startDateLong.value)
        }
    }

    val endDateString = remember {                                              // String Date
        derivedStateOf {
            dateInString(endDateLong.value)
        }
    }

    // Organizes the text in the screen vertically.
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(){
            Text("Plan Your ",style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Start)
            Text("Success",style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Start, color = lightBlue)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Let's set up your semester and get you organized for your academic journey.",style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(25.dp))

        // Text Field for Semester Name
        OutlinedTextField(
            value = semesterName.value,
            onValueChange = {semesterName.value = it},
            label = { Text("Semester Name")},
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Default.EditNote, contentDescription = "Name Icon")
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // START DATE
        Box(modifier = Modifier.fillMaxWidth()) {
            // Text Field for Schedule
            OutlinedTextField(
                value = startDateString.value,
                onValueChange = {},
                label = { Text(text = "Start Date") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Calendar(start) Icon")
                },
                trailingIcon = {
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Arrow Down Icon")
                }
            )

            Box(modifier = Modifier.matchParentSize().clickable{openCalendar(context){ datePicked ->
                startDateLong.value = datePicked
            }})
        }

        Spacer(modifier = Modifier.height(10.dp))

        // END DATE
        Box(modifier = Modifier.fillMaxWidth()) {
            // Text Field for Schedule
            OutlinedTextField(
                value = endDateString.value,
                onValueChange = {},
                label = { Text(text = "End Date") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.EventAvailable, contentDescription = "Calendar(end) Icon")
                },
                trailingIcon = {
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Arrow Down Icon")
                }
            )

            Box(modifier = Modifier.matchParentSize().clickable{openCalendar(context){ datePicked ->
                endDateLong.value = datePicked
            }})
        }

        Spacer(modifier = Modifier.height(150.dp))

        // Save Button
        Button(modifier = Modifier.fillMaxWidth().height(45.dp),
            colors = ButtonDefaults.buttonColors(containerColor = lightBlue),
            shape = RoundedCornerShape(15.dp),
            onClick = {
                val start = startDateLong.value
                val end = endDateLong.value

                if(start != null && end != null){
                    mainViewModel.saveSemester(semesterName.value, start, end)
                    mainViewModel.finishSetup()
                }
            }
        ){
            Row(){
                Text("Get Started", fontSize = 15.sp)

                Spacer(modifier = Modifier.width(10.dp))

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// Opens the calendar
fun openCalendar(context: Context, onDatePicked: (Long) -> Unit) {
    val calendar = Calendar.getInstance()

    DatePickerDialog(
        context,
        { _, year, month, day ->
            val picked = Calendar.getInstance()
            picked.set(year, month, day)

            onDatePicked(picked.timeInMillis)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

fun dateInString(date: Long?): String{
    if(date != null){
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date(date))
    }
    return ""
}