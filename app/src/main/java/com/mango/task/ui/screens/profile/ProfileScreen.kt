package com.mango.task.ui.screens.profile

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mango.task.R
import com.mango.task.ui.navigation.AppNavItems
import com.mango.task.ui.screens.profile.components.ProfileContent
import com.mango.task.ui.screens.profile.components.ProfileHeader

@SuppressLint("UseOfNonLambdaOffsetOverload")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    var base64: String? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isLoading)
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.viewEvent.collect { viewEvent ->
            when (viewEvent) {
                is ProfileEvent.ShowMessage -> {
                    Toast.makeText(context, viewEvent.message, Toast.LENGTH_SHORT).show()
                }

                ProfileEvent.NavigateToLogin -> {
                    navController.navigate(AppNavItems.EnterPhoneNumber.route) {
                        popUpTo(0)
                    }
                }
            }
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.profile_page_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    if (state.isEditing) {
                        IconButton(onClick = { viewModel.handleIntent(ProfileIntent.ExitEditMode) }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                tint = Color.White,
                                contentDescription = stringResource(R.string.close_editing_button_text)
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (!state.isEditing) {
                FloatingActionButton(
                    onClick = { viewModel.handleIntent(ProfileIntent.EditProfile) },
                    containerColor = MaterialTheme.colorScheme.secondary,
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = stringResource(R.string.edit_profile_button_text)
                    )
                }
            }
        }
    ) { paddingValues ->
        SwipeRefresh(
            modifier = Modifier.padding(paddingValues),
            indicator = { state, refreshTrigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = refreshTrigger,
                    scale = true,
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                )
            },
            state = swipeRefreshState,
            swipeEnabled = !state.isEditing,
            onRefresh = {
                viewModel.handleIntent(ProfileIntent.RefreshProfile)
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                ProfileHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-scrollState.value / 8).dp),
                    state = state,
                    scrollState = scrollState,
                ) { base64 = it }

                Column(modifier = Modifier.fillMaxSize()) {
                    ProfileContent(
                        state = state,
                        onSave = { updatedState ->
                            viewModel.handleIntent(
                                ProfileIntent.UpdateProfile(
                                    fullName = updatedState.fullName,
                                    dateOfBirth = updatedState.dateOfBirth,
                                    biography = updatedState.biography,
                                    city = updatedState.city,
                                    base64 = base64,
                                )
                            )
                        }
                    ) { viewModel.handleIntent(ProfileIntent.Logout) }
                }
            }
        }
    }
}