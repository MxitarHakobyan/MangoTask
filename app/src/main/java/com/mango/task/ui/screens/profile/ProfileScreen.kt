package com.mango.task.ui.screens.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mango.task.R
import com.mango.task.ui.screens.profile.components.ProfileContent
import com.mango.task.ui.screens.profile.components.ProfileHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    var base64 by remember { mutableStateOf("") }
    val context = LocalContext.current

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.profile_page_title), fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
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
                    Icon(Icons.Filled.Edit, contentDescription = stringResource(R.string.edit_profile_button_text))
                }
            }
        }
    ) { paddingValues ->
        SwipeRefresh(
            state = swipeRefreshState,
            swipeEnabled = !state.isEditing,
            onRefresh = {
                viewModel.handleIntent(ProfileIntent.RefreshProfile)
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                ProfileHeader(state = state) {
                    base64 = it
                }
                ProfileContent(
                    state = state,
                    onSave = { updatedState ->
                        viewModel.handleIntent(
                            ProfileIntent.UpdateProfile(
                                fullName = updatedState.fullName,
                                username = updatedState.username,
                                dateOfBirth = updatedState.dateOfBirth,
                                biography = updatedState.biography,
                                city = updatedState.city,
                                base64 = base64,
                            )
                        )
                    }
                )
            }
        }

        if (state.errorMessage.isNotEmpty()) {
            Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}