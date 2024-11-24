package com.mango.task.ui.screens.profile

data class UserProfile(
    val name: String,
    val username: String,
    val phoneNumber: String,
    val avatarUrl: String?,
    val city: String,
    val dateOfBirth: String,
    val bio: String
) {
    val zodiacSign: String
        get() = calculateZodiacSign(dateOfBirth)
}

fun calculateZodiacSign(dateOfBirth: String): String {
    val (year, month, day) = dateOfBirth.split("-").map { it.toInt() }
    return when {
        (month == 1 && day >= 20) || (month == 2 && day <= 18) -> "Aquarius"
        else -> "Unknown"
    }
}