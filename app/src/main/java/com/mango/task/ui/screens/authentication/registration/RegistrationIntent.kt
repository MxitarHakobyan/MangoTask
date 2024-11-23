package com.mango.task.ui.screens.authentication.registration

sealed class RegistrationIntent {
    data class EnterFullName(val fullName: String) : RegistrationIntent()
    data class EnterUsername(val username: String) : RegistrationIntent()
    data object Register : RegistrationIntent()
}