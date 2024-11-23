package com.mango.task.data.model.error

import com.google.gson.annotations.SerializedName

data class ApiErrorResponse(
    @SerializedName("detail")
    val detail: List<ErrorDetail>
)