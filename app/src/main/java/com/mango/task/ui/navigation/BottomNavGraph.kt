package com.mango.task.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mango.task.ui.screens.BottomNavigationBar
import com.mango.task.ui.screens.HomeScreen
import com.mango.task.ui.screens.ProfileScreen

@Composable
fun BottomNavGraph(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItems.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItems.Home.route) { HomeScreen() }
            composable(BottomNavItems.Profile.route) { ProfileScreen() }
        }
    }
}