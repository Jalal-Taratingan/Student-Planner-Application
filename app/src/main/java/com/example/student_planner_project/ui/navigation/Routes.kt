// ROUTES OF THE APPLICATION

package com.example.student_planner_project.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.student_planner_project.ui.screens.SetupScreen
import com.example.student_planner_project.ui.viewmodels.MainViewModel

@Composable
fun Routes(mainViewModel: MainViewModel){
    // Manages the switching of screens.
    val navController = rememberNavController()

    // Makes the display of screen reactive/dynamic for changes.
    val semester = mainViewModel.semester.collectAsState()

    // Determine what screen should be displayed after opening the application.
    val startingScreen = if(semester.value == null){
        Screen.Setup.route
    }else{
        Screen.Home.route
    }

    // Container that contains the list of the routes.
    NavHost(navController = navController, startDestination = startingScreen){
        composable(Screen.Setup.route){
            SetupScreen(mainViewModel)
        }

        composable(Screen.Home.route){
            Text("Home")
        }

        composable(Screen.Events.route){
            Text("Home")
        }

        composable(Screen.Tasks.route){
            Text("Home")
        }

        composable(Screen.Notepad.route){
            Text("Home")
        }

        composable(Screen.StudyTimer.route){
            Text("Home")
        }
    }
}