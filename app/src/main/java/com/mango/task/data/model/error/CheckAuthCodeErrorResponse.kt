package com.mango.task.data.model.error

import com.google.gson.annotations.SerializedName

data class CheckAuthCodeErrorResponse(
    @SerializedName("detail")
    val detail: AuthCodeErrorDetail
)

data class AuthCodeErrorDetail(
    @SerializedName("message")
    val message: String
)
