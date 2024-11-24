package com.mango.task.data.model.request

data class ProfileUpdateRequest(
    val name: String,
    val username: String,
    val birthday: String,
    val city: String,
    val status: String?,
    val avatar: AvatarData?
)

data class AvatarData(
    val filename: String,
    val base_64: String
)