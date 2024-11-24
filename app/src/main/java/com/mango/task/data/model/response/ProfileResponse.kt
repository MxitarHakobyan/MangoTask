package com.mango.task.data.model.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("profile_data") val profileData: ProfileData
)

data class ProfileData(
    val id: Long,
    val name: String,
    val username: String,
    val birthday: String,
    val city: String,
    val status: String?,
    val avatar: String,
    val online: Boolean,
    val phone: String,
    val avatars: Avatars
)

data class Avatars(
    val avatar: String,
    val bigAvatar: String,
    val miniAvatar: String
)