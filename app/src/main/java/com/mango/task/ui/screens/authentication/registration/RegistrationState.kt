package com.mango.task.ui.screens.authentication.registration

data class RegistrationState(
    val phoneNumber: String = "",
    val fullName: String = "",
    val username: String = "",
    val isUsernameValid: Boolean = true,
    val isRegisterEnabled: Boolean = false,
    val isLoading: Boolean = false,
)