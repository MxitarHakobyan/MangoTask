package com.mango.task.data.model.response

import com.google.gson.annotations.SerializedName

data class SendAuthCodeResponse(
    @SerializedName(value = "is_success")
    val isSuccess: Boolean
)
