package com.mango.task.data.model.error

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("detail")
    val detail: ErrorDetail
)

data class ErrorDetail(
    @SerializedName("message")
    val message: String
)
