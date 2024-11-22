package com.mango.task.ui.screens.authentication

sealed class RegistrationIntent {
    data object Submit : RegistrationIntent()
    data class PhoneNumberChanged(val phoneNumber: String) : RegistrationIntent()
    data class CountryCodeChanged(val countryCode: String) : RegistrationIntent()
}