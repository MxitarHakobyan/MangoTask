package com.mango.task.ui.screens.authentication.enterPhoneNumber

sealed class EnterPhoneNumberEvent {
    data object PhoneNumberSubmittedSuccessfully : EnterPhoneNumberEvent()
    data class PhoneNumberSubmissionFailed(val error: String) : EnterPhoneNumberEvent()
}