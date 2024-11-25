package com.mango.task.data.repository.mapper

import com.mango.task.data.model.local.ProfileEntity
import com.mango.task.data.model.local.calculateZodiacSign
import com.mango.task.data.model.response.ProfileData
import com.mango.task.ui.screens.profile.ProfileState

fun ProfileData.toEntity(): ProfileEntity {
    return ProfileEntity(
        id = id,
        name = name ?: "",
        username = username,
        birthday = birthday ?: "",
        city = city ?: "",
        status = status ?: "",
        avatar = avatar ?: "",
        phone = phone,
        avatarUrl = avatars?.avatar ?: "",
        bigAvatarUrl = avatars?.bigAvatar ?: "",
        miniAvatarUrl = avatars?.miniAvatar ?: "",
    )
}

fun ProfileEntity.toProfileState(): ProfileState {
    return ProfileState(
        fullName = name,
        username = username,
        dateOfBirth = birthday,
        zodiacSign = calculateZodiacSign(dateOfBirth = birthday),
        city = city,
        biography = status,
        avatarUrl = avatar,
        phoneNumber = phone,
        miniAvatarUrl = miniAvatarUrl,
    )
}