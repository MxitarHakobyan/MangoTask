package com.mango.task.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mango.task.ui.screens.authentication.enterAuthCode.EnterAuthCodeScreen
import com.mango.task.ui.screens.authentication.enterPhoneNumber.EnterPhoneNumber
import com.mango.task.ui.screens.authentication.registration.RegistrationScreen

const val PHONE_NUMBER_KEY = "phoneNumber"

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val navViewModel: NavViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = if (navViewModel.isLoggedIn()) {
            AppNavItems.BottomNavNavigation.route
        } else AppNavItems.EnterPhoneNumber.route
    ) {
        composable(route = AppNavItems.EnterPhoneNumber.route) { EnterPhoneNumber(navController = navController) }

        composable(
            route = "${AppNavItems.EnterAuthCode.route}/{${PHONE_NUMBER_KEY}}",
            arguments = listOf(navArgument(PHONE_NUMBER_KEY) { type = NavType.StringType }),
        ) { EnterAuthCodeScreen(navController = navController) }

        composable(
            route = "${AppNavItems.Registration.route}/{${PHONE_NUMBER_KEY}}",
            arguments = listOf(navArgument(PHONE_NUMBER_KEY) { type = NavType.StringType }),
        ) { RegistrationScreen(navController = navController) }

        composable(route = AppNavItems.BottomNavNavigation.route) { BottomNavGraph(navController = navController) }
    }
}