package com.mango.task.data.model.error

import com.google.gson.annotations.SerializedName

data class ApiErrorsResponse(
    @SerializedName("detail")
    val detail: List<ErrorsDetail>
)