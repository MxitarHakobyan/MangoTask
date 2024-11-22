package com.mango.task.ui.screens.authentication.enterPhoneNumber

sealed class EnterPhoneNumberIntent {
    data object Submit : EnterPhoneNumberIntent()
    data class PhoneNumberChanged(val phoneNumber: String) : EnterPhoneNumberIntent()
    data class CountryCodeChanged(val countryCode: String) : EnterPhoneNumberIntent()
}