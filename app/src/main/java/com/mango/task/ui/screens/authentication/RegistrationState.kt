package com.mango.task.ui.screens.authentication

data class RegistrationState(
    val isLoading: Boolean = false,
    val phoneNumber: String = "",
    val countryCode: String = "+7",
    val isPhoneValid: Boolean = true,
    val isRegistrationSuccessful: Boolean = false
)