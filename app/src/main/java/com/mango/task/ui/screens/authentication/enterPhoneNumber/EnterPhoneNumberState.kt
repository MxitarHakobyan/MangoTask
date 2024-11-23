package com.mango.task.ui.screens.authentication.enterPhoneNumber

data class EnterPhoneNumberState(
    val isLoading: Boolean = false,
    val phoneNumber: String = "",
    val countryCode: String = "+7",
    val isPhoneValid: Boolean = true,
)