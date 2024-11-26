package com.mango.task.ui.screens.profile

sealed class ProfileIntent {
    data object EditProfile : ProfileIntent()
    data object RefreshProfile : ProfileIntent()
    data object ExitEditMode : ProfileIntent()
    data class UpdateProfile(
        val fullName: String,
        val dateOfBirth: String,
        val biography: String,
        val city: String,
        val base64: String?,
    ) : ProfileIntent()
}