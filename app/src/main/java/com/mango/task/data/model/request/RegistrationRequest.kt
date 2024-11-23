package com.mango.task.data.model.request

import com.google.gson.annotations.SerializedName

data class RegistrationRequest(
    @SerializedName("phone")
    val phone: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("username")
    val username: String,
)