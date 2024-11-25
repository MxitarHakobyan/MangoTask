package com.mango.task.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.isEditing) {
            EditableTextField(
                label = "Full Name",
                value = fullName,
                onValueChange = { fullName = it })
            EditableTextField(
                label = "Username",
                value = username,
                onValueChange = { username = it })
            EditableTextField(
                label = "Date of Birth",
                value = dateOfBirth,
                onValueChange = { dateOfBirth = it })
            EditableTextField(
                label = "Biography",
                value = biography,
                onValueChange = { biography = it })
            EditableTextField(label = "City", value = city, onValueChange = { city = it })

            Button(
                onClick = {
                    onSave(
                        state.copy(
                            fullName = fullName,
                            username = username,
                            dateOfBirth = dateOfBirth,
                            biography = biography,
                            city = city,
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            ProfileDetails(state = state)
        }
    }
}

@Composable
fun ProfileDetails(state: ProfileState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ProfileRow(
            label = "Full Name",
            value = state.fullName,
            icon = Icons.Default.Person
        )
        ProfileRow(
            label = "Username",
            value = state.username,
            icon = Icons.Default.AccountCircle
        )
        ProfileRow(
            label = "Date of Birth",
            value = state.dateOfBirth,
            icon = Icons.Default.DateRange
        )
        ProfileRow(
            label = "Zodiac Sign",
            value = state.zodiacSign,
            icon = Icons.Default.Star
        )
        ProfileRow(
            label = "Biography",
            value = state.biography,
            icon = Icons.Default.Info
        )
        ProfileRow(
            label = "City",
            value = state.city,
            icon = Icons.Default.LocationOn
        )
        ProfileRow(
            label = "Phone",
            value = state.phoneNumber,
            icon = Icons.Default.Phone
        )
    }
}

@Composable
fun ProfileRow(label: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "$label icon",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            Text(
                text = value.ifEmpty { "..." },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium
            )
        }
    }
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