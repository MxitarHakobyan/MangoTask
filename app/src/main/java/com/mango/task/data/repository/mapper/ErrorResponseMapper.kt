package com.mango.task.data.repository.mapper

import com.google.gson.Gson
import com.mango.task.data.model.error.ErrorResponse

fun parseError(errorBody: String?): String {
    val errorResponse = errorBody?.let { errorJson ->
        Gson().fromJson(errorJson, ErrorResponse::class.java)
    }
    return errorResponse?.detail?.message ?: "Unknown error"
}