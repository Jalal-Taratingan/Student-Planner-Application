// BOTTOM DRAWER

package com.example.student_planner_project.ui.screens

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.student_planner_project.R
import com.example.student_planner_project.ui.navigation.Routes
import com.example.student_planner_project.ui.navigation.Screen
import com.example.student_planner_project.ui.theme.darkBlue
import com.example.student_planner_project.ui.viewmodels.MainViewModel

@Composable
fun Tabs(mainViewModel: MainViewModel){
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val backgroundColor = Color(0xFFF8F9FF)

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = backgroundColor, tonalElevation = 0.dp, modifier = Modifier.height(80.dp), windowInsets = WindowInsets(0, 0, 0, 0)){

                // TAB ITEMS:
                // Home
                NavigationBarItem(
                    label = { Text("Home") },
                    onClick = {navController.navigate(Screen.Home.route)},
                    icon = { Icon(painter = painterResource(id = R.drawable.home), contentDescription = null, modifier = Modifier.size(24.dp)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = darkBlue,
                        selectedTextColor = darkBlue,
                        indicatorColor = Color.Transparent
                    ),
                    selected =
                        if(currentRoute == Screen.Home.route){
                            true
                        }else{
                            false
                        }
                )

                // Subjects
                NavigationBarItem(
                    label = {Text("Subjects") },
                    onClick = { navController.navigate(Screen.Subjects.route)},
                    icon = {Icon(painter = painterResource(id = R.drawable.subjects) , contentDescription = null, modifier = Modifier.size(24.dp))},
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = darkBlue,
                        selectedTextColor = darkBlue,
                        indicatorColor = Color.Transparent
                    ),
                    selected =
                        if(currentRoute == Screen.Subjects.route){
                            true
                        }else{
                            false
                        }
                )

                // Tasks
                NavigationBarItem(
                    label = { Text("Tasks") },
                    onClick = {navController.navigate(Screen.Tasks.route)},
                    icon = { Icon(painter = painterResource(id = R.drawable.tasks), contentDescription = null, modifier = Modifier.size(24.dp))},
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = darkBlue,
                        selectedTextColor = darkBlue,
                        indicatorColor = Color.Transparent
                    ),
                    selected =
                        if(currentRoute == Screen.Tasks.route){
                            true
                        }else{
                            false
                        }
                )

                // Notes
                NavigationBarItem(
                    label = { Text("Notes")},
                    onClick = {navController.navigate(Screen.Notes.route)},
                    icon = {Icon(painter = painterResource(id = R.drawable.notes), contentDescription = null, modifier = Modifier.size(24.dp))},
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = darkBlue,
                        selectedTextColor = darkBlue,
                        indicatorColor = Color.Transparent
                    ),
                    selected =
                        if(currentRoute == Screen.Notes.route){
                            true
                        }else{
                            false
                        }
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


