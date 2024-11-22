package com.mango.task.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.mango.task.R

sealed class BottomNavItems(val route: String, val title: Int, val icon: ImageVector) {
    data object Home : BottomNavItems(
        route = "home",
        title = R.string.bottom_nav_home_title,
        icon = Icons.Default.Home
    )

    data object Profile : BottomNavItems(
        route = "profile",
        title = R.string.bottom_nav_profile_title,
        icon = Icons.Default.Person
    )
}