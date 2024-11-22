package com.mango.task.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mango.task.ui.screens.authentication.RegistrationScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppNavItems.Registration.route
    ) {
        composable(route = AppNavItems.Registration.route) { RegistrationScreen(navController = navController) }
        composable(route = AppNavItems.BottomNavNavigation.route) { BottomNavGraph() }
    }
}