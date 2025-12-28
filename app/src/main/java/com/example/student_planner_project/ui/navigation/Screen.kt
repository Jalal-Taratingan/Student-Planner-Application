package com.example.student_planner_project.ui.navigation

sealed class Screen(val route : String){
    object Setup : Screen("setup")
    object Home : Screen("home")
    object Tasks : Screen("tasks")
    object Events : Screen("events")
    object Notepad : Screen("notepad")
    object StudyTimer : Screen("study_timer")
}