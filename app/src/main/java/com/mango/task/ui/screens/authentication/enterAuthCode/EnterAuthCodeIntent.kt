package com.mango.task.ui.screens.authentication.enterAuthCode

sealed class AuthCodeIntent {
    data class EnterAuthCode(val code: String) : AuthCodeIntent()
    data object SubmitCode : AuthCodeIntent()
}