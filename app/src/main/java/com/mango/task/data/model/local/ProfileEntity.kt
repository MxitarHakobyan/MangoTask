package com.mango.task.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_table")
data class ProfileEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val username: String,
    val birthday: String,
    val city: String,
    val status: String,
    val avatar: String,
    val phone: String,
    val avatarUrl: String,
    val bigAvatarUrl: String,
    val miniAvatarUrl: String
)

@Suppress("KotlinConstantConditions")
fun calculateZodiacSign(dateOfBirth: String): String {
    if (dateOfBirth.isEmpty()) return ""

    val (_, month, day) = dateOfBirth.split("-").map { it.toInt() }

    return when {
        (month == 1 && day >= 20) || (month == 2 && day <= 18) -> "Aquarius"
        (month == 2 && day >= 19) || (month == 3 && day <= 20) -> "Pisces"
        (month == 3 && day >= 21) || (month == 4 && day <= 19) -> "Aries"
        (month == 4 && day >= 20) || (month == 5 && day <= 20) -> "Taurus"
        (month == 5 && day >= 21) || (month == 6 && day <= 20) -> "Gemini"
        (month == 6 && day >= 21) || (month == 7 && day <= 22) -> "Cancer"
        (month == 7 && day >= 23) || (month == 8 && day <= 22) -> "Leo"
        (month == 8 && day >= 23) || (month == 9 && day <= 22) -> "Virgo"
        (month == 9 && day >= 23) || (month == 10 && day <= 22) -> "Libra"
        (month == 10 && day >= 23) || (month == 11 && day <= 21) -> "Scorpio"
        (month == 11 && day >= 22) || (month == 12 && day <= 21) -> "Sagittarius"
        (month == 12 && day >= 22) || (month == 1 && day <= 19) -> "Capricorn"
        else -> "Unknown"
    }
}