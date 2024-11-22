package com.mango.task.ui.navigation

sealed class AppNavItems(val route: String) {
    data object Registration : AppNavItems(route = "registration")
    data object BottomNavNavigation : AppNavItems(route = "bottomNavNavigation")
}