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

fun calculateZodiacSign(dateOfBirth: String): String {
    if (dateOfBirth.isEmpty()) return ""
    val (_, month, day) = dateOfBirth.split("-").map { it.toInt() }
    return when {
        (month == 1 && day >= 20) || (month == 2 && day <= 18) -> "Aquarius"
        else -> "Unknown"
    }
}