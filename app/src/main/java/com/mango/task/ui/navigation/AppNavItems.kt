package com.mango.task.ui.navigation

sealed class AppNavItems(val route: String) {
    data object EnterPhoneNumber : AppNavItems(route = "enterPhoneNumber")
    data object BottomNavNavigation : AppNavItems(route = "bottomNavNavigation")
}