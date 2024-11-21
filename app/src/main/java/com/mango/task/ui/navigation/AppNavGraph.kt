package com.mango.task.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mango.task.ui.screens.RegistrationScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "registration"
    ) {
        composable("registration") { RegistrationScreen(navController) }
        composable("main") { BottomNavGraph() }
    }
}