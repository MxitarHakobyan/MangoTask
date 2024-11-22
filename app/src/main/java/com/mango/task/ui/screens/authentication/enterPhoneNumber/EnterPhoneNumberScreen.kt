package com.mango.task.ui.screens.authentication.enterPhoneNumber


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mango.task.R
import com.mango.task.ui.navigation.AppNavItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterPhoneNumber(
    navController: NavController,
    enterPhoneNumberViewModel: EnterPhoneNumberViewModel = hiltViewModel()
) {
    val state by enterPhoneNumberViewModel.state

    val countryList = Country.countryList
    val placeholderCountry = Country.placeholderCountry
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(state.isAuthCodeSend) {
        if (state.isAuthCodeSend) {
            navController.navigate(AppNavItems.BottomNavNavigation.route) {
                popUpTo(AppNavItems.EnterPhoneNumber.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(stringResource(R.string.enter_phone_number_page_title)) })
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.enter_your_phone_number_message),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(32.dp))

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                val (flagButton, codeField, phoneField, errorMessage) = createRefs()

                Box(modifier = Modifier.constrainAs(flagButton) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }) {
                    Image(
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .size(32.dp)
                            .clickable { expanded = true },
                        painter = painterResource(
                            id = countryList.find { it.code == state.countryCode }?.flagRes
                                ?: placeholderCountry.flagRes
                        ),
                        contentDescription = "Flag"
                    )

                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        countryList.forEach { country ->
                            DropdownMenuItem(text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(id = country.flagRes),
                                        contentDescription = stringResource(country.name),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("${country.code} (${stringResource(country.name)})")
                                }
                            }, onClick = {
                                enterPhoneNumberViewModel.processIntent(
                                    intent = EnterPhoneNumberIntent.CountryCodeChanged(countryCode = country.code)
                                )
                                expanded = false
                            })
                        }
                    }
                }

                OutlinedTextField(
                    value = state.countryCode,
                    onValueChange = {
                        enterPhoneNumberViewModel.processIntent(
                            intent = EnterPhoneNumberIntent.CountryCodeChanged(
                                countryCode = it
                            )
                        )
                    },
                    label = { Text(stringResource(R.string.country_code_input_title)) },
                    modifier = Modifier
                        .width(100.dp)
                        .height(64.dp)
                        .constrainAs(codeField) {
                            start.linkTo(flagButton.end, 8.dp)
                            top.linkTo(flagButton.top)
                            bottom.linkTo(flagButton.bottom)
                        })

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .constrainAs(phoneField) {
                            start.linkTo(codeField.end, 8.dp)
                            end.linkTo(parent.end)
                            top.linkTo(codeField.top)
                            bottom.linkTo(codeField.bottom)
                            width = Dimension.fillToConstraints
                        },
                    value = state.phoneNumber,
                    onValueChange = {
                        enterPhoneNumberViewModel.processIntent(
                            intent = EnterPhoneNumberIntent.PhoneNumberChanged(
                                phoneNumber = it
                            )
                        )
                    },
                    label = { Text(stringResource(R.string.phone_number_input_title)) },
                    isError = !state.isPhoneValid,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Phone
                    )
                )
                if (!state.isPhoneValid) {
                    Text(
                        text = stringResource(R.string.phone_number_input_error_message),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.constrainAs(errorMessage) {
                            linkTo(start = phoneField.start, end = phoneField.end, bias = 0F)
                            top.linkTo(anchor = phoneField.bottom, margin = 4.dp)
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { enterPhoneNumberViewModel.processIntent(EnterPhoneNumberIntent.Submit) },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isPhoneValid && !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(32.dp)
                            .padding(top = 4.dp, end = 8.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                } else {
                    Text(stringResource(R.string.submit_button_text))
                }
            }
        }
    }
}