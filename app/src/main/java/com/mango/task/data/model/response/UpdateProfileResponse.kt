package com.mango.task.data.model.response

import com.google.gson.annotations.SerializedName

class UpdateProfileResponse(
    @SerializedName("avatars")
    val avatars: Avatars?,
)
