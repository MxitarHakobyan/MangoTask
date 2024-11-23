package com.mango.task.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mango.task.ui.screens.authentication.enterAuthCode.EnterAuthCodeScreen
import com.mango.task.ui.screens.authentication.enterPhoneNumber.EnterPhoneNumber

const val PHONE_NUMBER_KEY = "phoneNumber"

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppNavItems.EnterPhoneNumber.route
    ) {
        composable(route = AppNavItems.EnterPhoneNumber.route) { EnterPhoneNumber(navController = navController) }

        composable(
            route = "${AppNavItems.EnterAuthCode.route}/{${PHONE_NUMBER_KEY}}",
            arguments = listOf(navArgument(PHONE_NUMBER_KEY) { type = NavType.StringType }),
        ) { EnterAuthCodeScreen(navController = navController) }

        composable(route = AppNavItems.BottomNavNavigation.route) { BottomNavGraph() }
    }
}