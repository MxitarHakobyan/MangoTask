package com.mango.task.ui.screens.authentication.enterAuthCode

data class EnterAuthCodeState(
    val authCode: String = "",
    val isCodeValid: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)