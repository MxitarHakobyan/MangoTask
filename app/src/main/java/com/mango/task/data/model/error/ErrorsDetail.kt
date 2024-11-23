package com.mango.task.data.model.error

import com.google.gson.annotations.SerializedName

data class ErrorsDetail(
    @SerializedName("loc")
    val loc: List<String>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("type")
    val type: String,
)