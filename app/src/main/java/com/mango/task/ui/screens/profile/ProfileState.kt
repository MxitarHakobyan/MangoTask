package com.mango.task.ui.screens.profile

data class ProfileState(
    val fullName: String = "",
    val username: String = "",
    val dateOfBirth: String = "",
    val zodiacSign: String = "",
    val biography: String = "",
    val city: String = "",
    val avatarUrl: String = "",
    val miniAvatarUrl: String = "",
    val phoneNumber: String = "",
    val isEditing: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
)