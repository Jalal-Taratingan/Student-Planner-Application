// BOTTOM DRAWER

package com.example.student_planner_project.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.student_planner_project.R
import com.example.student_planner_project.ui.navigation.Routes
import com.example.student_planner_project.ui.navigation.Screen
import com.example.student_planner_project.ui.viewmodels.MainViewModel

@Composable
fun Tabs(mainViewModel: MainViewModel){
    val navController = rememberNavController()

//    val navBackStackEntry = navController.currentBackStackEntryAsState()
//    val currentScreen = navBackStackEntry.value?.destination?.route
    Scaffold(
        bottomBar = {
            NavigationBar{

                // TAB ITEMS:
                // Home
                NavigationBarItem(
                    label = { Text("Home") },
//                    selected = currentScreen == "home",
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.Home.route)
                    },
                    icon = { Icon(painter = painterResource(id = R.drawable.home), contentDescription = null, modifier = Modifier.size(24.dp)) }
                )

                NavigationBarItem(
                    label = {Text("Subjects") },
                    selected = false,
                    onClick = { navController.navigate(Screen.Subjects.route)},
                    icon = {Icon(painter = painterResource(id = R.drawable.subjects) , contentDescription = null, modifier = Modifier.size(24.dp))}
                )

                // Tasks
                NavigationBarItem(
                    label = { Text("Tasks") },
//                    selected = currentScreen == "tasks",
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.Tasks.route)
                    },
                    icon = { Icon(painter = painterResource(id = R.drawable.tasks), contentDescription = null, modifier = Modifier.size(24.dp)) }
                )

                // Notes
                NavigationBarItem(
                    label = { Text("Notes")},
                    selected = false,
                    onClick = {navController.navigate(Screen.Notepad.route)},
                    icon = {Icon(painter = painterResource(id = R.drawable.notes), contentDescription = null, modifier = Modifier.size(24.dp))}
                )
            }
        }
    ) { paddingValues ->
            Routes(
                navController = navController,
                mainViewModel = mainViewModel,
                modifier = Modifier.padding(paddingValues)
            )
    }
}


