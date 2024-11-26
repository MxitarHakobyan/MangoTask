package com.mango.task.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.mango.task.R

sealed class BottomNavItems(val route: String, val title: Int, val icon: ImageVector) {
    data object Chatlist : BottomNavItems(
        route = "chatList",
        title = R.string.bottom_nav_chat_list_title,
        icon = Icons.Default.MailOutline
    )

    data object Profile : BottomNavItems(
        route = "profile",
        title = R.string.bottom_nav_profile_title,
        icon = Icons.Default.Person
    )
}