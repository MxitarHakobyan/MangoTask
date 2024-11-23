package com.mango.task.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mango.task.ui.screens.authentication.enterAuthCode.EnterAuthCodeScreen
import com.mango.task.ui.screens.authentication.enterPhoneNumber.EnterPhoneNumber

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppNavItems.EnterPhoneNumber.route
    ) {
        composable(route = AppNavItems.EnterPhoneNumber.route) { EnterPhoneNumber(navController = navController) }
        composable(route = AppNavItems.EnterAuthCode.route) { EnterAuthCodeScreen(navController = navController) }
        composable(route = AppNavItems.BottomNavNavigation.route) { BottomNavGraph() }
    }
}