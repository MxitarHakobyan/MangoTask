package com.mango.task.data.model.request

import com.google.gson.annotations.SerializedName

data class CheckAuthCodeRequest(
    @SerializedName("phone")
    val phone: String,
    @SerializedName("code")
    val code: String,
)