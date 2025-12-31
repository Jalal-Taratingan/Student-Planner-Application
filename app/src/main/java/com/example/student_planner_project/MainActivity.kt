package com.example.student_planner_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.student_planner_project.ui.navigation.Routes
import com.example.student_planner_project.ui.theme.StudentPlannerProjectTheme
import com.example.student_planner_project.ui.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentPlannerProjectTheme {
                val mainViewModel: MainViewModel = viewModel()
                Routes(mainViewModel = mainViewModel)
            }
        }
    }
}