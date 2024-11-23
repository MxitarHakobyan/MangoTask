package com.mango.task.ui.screens.authentication.registration

sealed class RegistrationEvent {
    data object UserCreated : RegistrationEvent()
    data class FailedToCreateUser(val error: String) : RegistrationEvent()
}