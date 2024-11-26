package com.mango.task.ui.screens.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mango.task.R
import com.mango.task.ui.screens.profile.ProfileState

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    state: ProfileState,
    onSave: (ProfileState) -> Unit,
    onLogoutClicked: () -> Unit,
) {
    var fullName by remember { mutableStateOf(state.fullName) }
    var dateOfBirth by remember { mutableStateOf(state.dateOfBirth) }
    var biography by remember { mutableStateOf(state.biography) }
    var city by remember { mutableStateOf(state.city) }

    LaunchedEffect(state) {
        fullName = state.fullName
        dateOfBirth = state.dateOfBirth
        biography = state.biography
        city = state.city
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.isEditing) {
            EditableTextField(
                modifier= Modifier.padding(top = 16.dp),
                label = stringResource(R.string.full_name_label),
                value = fullName,
                onValueChange = { fullName = it })
            CalendarPickerTextField(
                label = stringResource(id = R.string.dob_label),
                value = dateOfBirth,
                onValueChange = { dateOfBirth = it })
            EditableTextField(
                label = stringResource(id = R.string.biography_label),
                value = biography,
                onValueChange = { biography = it })
            EditableTextField(
                label = stringResource(id = R.string.city_label),
                value = city,
                onValueChange = { city = it }
            )

            Button(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 16.dp, start = 24.dp, end = 24.dp),
                onClick = {
                    onSave(
                        state.copy(
                            fullName = fullName,
                            dateOfBirth = dateOfBirth,
                            biography = biography,
                            city = city,
                        )
                    )
                },
            ) {
                Text(stringResource(id = R.string.save_button), style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            ProfileDetails(state = state) {
                onLogoutClicked()
            }
        }
    }
}