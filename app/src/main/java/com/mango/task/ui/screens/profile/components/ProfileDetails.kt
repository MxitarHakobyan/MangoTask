package com.mango.task.ui.screens.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mango.task.R
import com.mango.task.ui.screens.profile.ProfileState

@Composable
fun ProfileDetails(
    modifier: Modifier = Modifier,
    state: ProfileState,
    onLogoutClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = stringResource(R.string.profile_details),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        ProfileRow(
            label = stringResource(R.string.full_name_label),
            value = state.fullName,
            icon = Icons.Default.Person
        )
        ProfileRow(
            label = stringResource(R.string.username_label),
            value = state.username,
            icon = Icons.Default.AccountCircle
        )
        ProfileRow(
            label = stringResource(R.string.dob_label),
            value = state.dateOfBirth,
            icon = Icons.Default.DateRange
        )
        ProfileRow(
            label = stringResource(R.string.zodiac_sign_label),
            value = state.zodiacSign,
            icon = Icons.Default.Star
        )
        ProfileRow(
            label = stringResource(R.string.biography_label),
            value = state.biography,
            icon = Icons.Default.Info
        )
        ProfileRow(
            label = stringResource(R.string.city_label),
            value = state.city,
            icon = Icons.Default.LocationOn
        )
        ProfileRow(
            label = stringResource(R.string.phone_label),
            value = state.phoneNumber,
            icon = Icons.Default.Phone
        )

        Button(
            modifier = Modifier
                .padding(top = 24.dp, start = 32.dp, end = 32.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
                disabledContainerColor = MaterialTheme.colorScheme.onError,
                disabledContentColor = MaterialTheme.colorScheme.onError
            ),
            onClick = { onLogoutClicked() },
        ) {
            Text(
                stringResource(id = R.string.logout_button),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileDetailsPreview() {
    MaterialTheme {
        ProfileDetails(
            state = ProfileState(
                fullName = "John Doe",
                username = "johndoe123",
                dateOfBirth = "1990-01-01",
                zodiacSign = "Capricorn",
                biography = "A passionate software developer.",
                city = "New York",
                phoneNumber = "+1 234 567 8900",
            ),
            onLogoutClicked = {},
        )
    }
}