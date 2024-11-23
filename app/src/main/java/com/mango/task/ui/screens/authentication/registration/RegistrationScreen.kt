package com.mango.task.ui.screens.authentication.registration

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mango.task.R
import com.mango.task.ui.navigation.AppNavItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    navController: NavController,
    viewModel: RegistrationViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is RegistrationEvent.UserCreated -> {
                    navController.navigate(AppNavItems.BottomNavNavigation.route) {
                        popUpTo(AppNavItems.EnterPhoneNumber.route) {
                            inclusive = true
                        }
                    }
                }

                is RegistrationEvent.FailedToCreateUser -> {
                    Toast.makeText(context, event.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(stringResource(R.string.registration_page_title)) }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.phoneNumber,
                onValueChange = {},
                label = { Text(stringResource(R.string.phone_number_text_title)) },
                enabled = false,
                singleLine = true,
                readOnly = true,
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.fullName,
                onValueChange = { viewModel.handleIntent(RegistrationIntent.EnterFullName(it)) },
                label = { Text(stringResource(R.string.full_name_input_title)) },
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.username,
                onValueChange = { viewModel.handleIntent(RegistrationIntent.EnterUsername(it)) },
                label = { Text(stringResource(R.string.username_input_title)) },
                isError = !state.isUsernameValid,
                singleLine = true,
            )

            if (!state.isUsernameValid) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 4.dp),
                    text = stringResource(R.string.username_input_error_message),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.handleIntent(RegistrationIntent.Register) },
                enabled = state.isRegisterEnabled && !state.isLoading,
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(32.dp)
                            .padding(top = 4.dp, end = 8.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                } else {
                    Text(stringResource(R.string.registration_button_text))
                }
            }
        }
    }
}