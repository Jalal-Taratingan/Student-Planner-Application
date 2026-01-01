// DRAWER ON THE LEFT SIDE OF THE SCREEN

package com.example.student_planner_project.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.student_planner_project.ui.navigation.Routes
import com.example.student_planner_project.ui.navigation.Screen
import com.example.student_planner_project.ui.viewmodels.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Drawer(mainViewModel: MainViewModel){
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

//    val navBackStackEntry = navController.currentBackStackEntryAsState()
//    val currentScreen = navBackStackEntry.value?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    "Menu",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
                HorizontalDivider()

                // Navigation Items
                NavigationDrawerItem(
                    label = { Text("Home") },
//                    selected = currentScreen == "home",
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.Home.route)
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) }
                )

                NavigationDrawerItem(
                    label = { Text("Tasks") },
//                    selected = currentScreen == "tasks",
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.Tasks.route)
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.List, contentDescription = null) }
                )
            }
        }
    ) { Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Student Planner") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
        ) { paddingValues ->
            Routes(
                navController = navController,
                mainViewModel = mainViewModel,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}
