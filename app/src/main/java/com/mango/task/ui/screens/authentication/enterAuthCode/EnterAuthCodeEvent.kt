package com.mango.task.ui.screens.authentication.enterAuthCode

sealed class EnterAuthCodeEvent {
    data object CodeSubmittedSuccessfully : EnterAuthCodeEvent()
    data class CodeSubmissionFailed(val error: String) : EnterAuthCodeEvent()
}