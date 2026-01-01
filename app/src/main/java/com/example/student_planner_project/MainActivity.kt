package com.example.student_planner_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.student_planner_project.ui.screens.Drawer
import com.example.student_planner_project.ui.screens.SetupScreen
import com.example.student_planner_project.ui.theme.StudentPlannerProjectTheme
import com.example.student_planner_project.ui.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentPlannerProjectTheme {
                val viewModel : MainViewModel = viewModel()
                val semester = viewModel.semester.collectAsState()

                if (semester.value == null) {
                    SetupScreen(viewModel)
                } else {
                    Drawer(viewModel)
                }
            }
        }
    }
}