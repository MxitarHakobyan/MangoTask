package com.mango.task.data.repository.mapper

import com.mango.task.data.model.local.ProfileEntity
import com.mango.task.data.model.response.ProfileData

fun ProfileData.toEntity(): ProfileEntity {
    return ProfileEntity(
        id = id,
        name = name,
        username = username,
        birthday = birthday,
        city = city,
        status = status,
        avatar = avatar,
        phone = phone,
        avatarUrl = avatars.avatar,
        bigAvatarUrl = avatars.bigAvatar,
        miniAvatarUrl = avatars.miniAvatar
    )
}