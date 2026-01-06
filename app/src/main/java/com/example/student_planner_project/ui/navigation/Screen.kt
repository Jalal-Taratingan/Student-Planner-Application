package com.example.student_planner_project.ui.navigation

sealed class Screen(val route : String){
    object Setup : Screen("setup")
    object Home : Screen("home")
    object Subjects : Screen("subjects")
    object Tasks : Screen("tasks")
    object Notes : Screen("notes")
}