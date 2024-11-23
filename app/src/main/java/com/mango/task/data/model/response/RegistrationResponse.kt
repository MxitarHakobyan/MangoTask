package com.mango.task.data.model.response

import com.google.gson.annotations.SerializedName

data class RegistrationResponse (
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("userId")
    val userId: Int,
)