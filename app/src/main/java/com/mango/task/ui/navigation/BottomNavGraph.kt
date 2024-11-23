package com.mango.task.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mango.task.ui.screens.BottomNavigationBar
import com.mango.task.ui.screens.ProfileScreen
import com.mango.task.ui.screens.home.HomeScreen

@Composable
fun BottomNavGraph(
    navController: NavController,
    nestedNavController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(nestedNavController) }
    ) { paddingValues ->
        NavHost(
            navController = nestedNavController,
            startDestination = BottomNavItems.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItems.Home.route) {
                HomeScreen(
                    navController = navController,
                    nestedNavController = nestedNavController
                )
            }
            composable(BottomNavItems.Profile.route) { ProfileScreen() }
        }
    }
}