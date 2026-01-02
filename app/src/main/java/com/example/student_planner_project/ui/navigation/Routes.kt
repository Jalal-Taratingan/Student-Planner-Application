// ROUTES OF THE APPLICATION

package com.example.student_planner_project.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.student_planner_project.ui.screens.HomeScreen
import com.example.student_planner_project.ui.screens.SetupScreen
import com.example.student_planner_project.ui.screens.TaskScreen
import com.example.student_planner_project.ui.viewmodels.MainViewModel

@Composable
fun Routes(navController: NavHostController, mainViewModel: MainViewModel, modifier: Modifier = Modifier){

    // Container that contains the list of the routes.
    NavHost(navController = navController, startDestination = Screen.Home.route, modifier = modifier) {
        composable(Screen.Setup.route) {
            SetupScreen(mainViewModel)
        }

        composable(Screen.Home.route) {
            HomeScreen(mainViewModel)
        }

        composable(Screen.Tasks.route) {
            TaskScreen(mainViewModel)
        }

        composable(Screen.Subjects.route){
            Text("Subjects")
        }

        composable(Screen.Notepad.route) {
            Text("Home")
        }
    }
}