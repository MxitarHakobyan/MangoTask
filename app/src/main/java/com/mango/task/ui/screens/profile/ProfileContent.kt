package com.mango.task.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ProfileContent(
    state: ProfileState,
    onSave: (ProfileState) -> Unit
) {
    var fullName by remember { mutableStateOf(state.fullName) }
    var username by remember { mutableStateOf(state.username) }
    var dateOfBirth by remember { mutableStateOf(state.dateOfBirth) }
    var biography by remember { mutableStateOf(state.biography) }
    var city by remember { mutableStateOf(state.city) }
    var phoneNumber by remember { mutableStateOf(state.phoneNumber) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.isEditing) {
            EditableTextField(label = "Full Name", value = fullName, onValueChange = { fullName = it })
            EditableTextField(label = "Username", value = username, onValueChange = { username = it })
            EditableTextField(label = "Date of Birth", value = dateOfBirth, onValueChange = { dateOfBirth = it })
            EditableTextField(label = "Biography", value = biography, onValueChange = { biography = it })
            EditableTextField(label = "City", value = city, onValueChange = { city = it })
            EditableTextField(label = "Phone Number", value = phoneNumber, onValueChange = { phoneNumber = it })

            Button(
                onClick = {
                    onSave(
                        state.copy(
                            fullName = fullName,
                            username = username,
                            dateOfBirth = dateOfBirth,
                            biography = biography,
                            city = city,
                            phoneNumber = phoneNumber
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            ProfileText("Full Name: $fullName")
            ProfileText("Username: @$username")
            ProfileText("Date of Birth: $dateOfBirth")
            ProfileText("Biography: $biography")
            ProfileText("City: $city")
            ProfileText("Phone: $phoneNumber")
        }
    }
}

@Composable
fun ProfileText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun EditableTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
        )
    }
}