package com.mango.task.data.model.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("profile_data")
    val profileData: ProfileData
)

data class ProfileData(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String?,
    @SerializedName("username")
    val username: String,
    @SerializedName("birthday")
    val birthday: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("avatar")
    val avatar: String?,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("avatars")
    val avatars: Avatars?
)

data class Avatars(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("bigAvatar")
    val bigAvatar: String,
    @SerializedName("miniAvatar")
    val miniAvatar: String
)