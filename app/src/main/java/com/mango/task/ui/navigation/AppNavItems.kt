package com.mango.task.ui.navigation

sealed class AppNavItems(val route: String) {
    data object SplashScreen : AppNavItems(route = "splashScreen")
    data object EnterPhoneNumber : AppNavItems(route = "enterPhoneNumber")
    data object EnterAuthCode : AppNavItems(route = "enterAuthCode")
    data object Registration : AppNavItems(route = "registration")
    data object BottomNavNavigation : AppNavItems(route = "bottomNavNavigation")
}