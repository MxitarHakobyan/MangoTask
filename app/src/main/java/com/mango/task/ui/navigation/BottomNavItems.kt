package com.mango.task.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItems(val route: String, val title: String, val icon: ImageVector) {
    data object Home : BottomNavItems(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )

    data object Profile : BottomNavItems(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )
}