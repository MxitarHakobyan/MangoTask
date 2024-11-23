package com.mango.task.ui.screens.authentication.enterAuthCode

sealed class EnterAuthCodeEvent {
    data class CodeSubmittedSuccessfully(val isUserExists: Boolean) : EnterAuthCodeEvent()
    data class CodeSubmissionFailed(val error: String) : EnterAuthCodeEvent()
}