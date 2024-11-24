package com.mango.task.ui.screens.profile

sealed class ProfileIntent {
    data object EditProfile : ProfileIntent()
    data object ExitEditMode : ProfileIntent()
    data class UpdateProfile(
        val fullName: String,
        val username: String,
        val dateOfBirth: String,
        val biography: String,
        val city: String,
        val avatarUrl: String,
        val phoneNumber: String
    ) : ProfileIntent()
}