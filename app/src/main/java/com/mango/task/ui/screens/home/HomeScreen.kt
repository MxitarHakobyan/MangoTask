package com.mango.task.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mango.task.ui.navigation.AppNavItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    nestedNavController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        homeViewModel.viewIntent.collect { viewIntent ->
            when (viewIntent) {
                is HomeViewIntent.ShowMessage -> {
                    Toast.makeText(context, viewIntent.message, Toast.LENGTH_SHORT).show()
                }

                HomeViewIntent.NavigateToLogin -> {
                    navController.navigate(AppNavItems.EnterPhoneNumber.route) {
                        popUpTo(0)
                    }
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Home") },
                actions = {
                    TextButton(onClick = {
                        homeViewModel.onEvent(HomeViewEvent.Logout(context = context))
                    }) {
                        Text(
                            text = "Logout",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Welcome to the Home Screen!",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}