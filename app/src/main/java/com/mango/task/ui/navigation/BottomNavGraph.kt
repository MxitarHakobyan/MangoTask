package com.mango.task.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mango.task.ui.screens.chat.ChatScreen
import com.mango.task.ui.screens.components.BottomNavigationBar
import com.mango.task.ui.screens.chatList.ChatListScreen
import com.mango.task.ui.screens.profile.ProfileScreen

@Composable
fun BottomNavGraph(
    navController: NavController,
    nestedNavController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(nestedNavController) }
    ) { paddingValues ->
        NavHost(
            navController = nestedNavController,
            startDestination = BottomNavItems.ChatList.route,
            modifier = Modifier.padding(paddingValues),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(700)
                )
            },
        ) {
            composable(BottomNavItems.ChatList.route) { ChatListScreen(nestedNavController) }
            composable(BottomNavItems.Chat.route) { ChatScreen(nestedNavController) }
            composable(BottomNavItems.Profile.route) { ProfileScreen(navController) }
        }
    }
}