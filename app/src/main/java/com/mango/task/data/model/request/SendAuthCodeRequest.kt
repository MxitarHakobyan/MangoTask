package com.mango.task.data.model.request

import com.google.gson.annotations.SerializedName

data class SendAuthCodeRequest(
    @SerializedName(value = "phone")
    val phone: String,
)
