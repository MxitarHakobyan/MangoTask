package com.mango.task.data.repository.mapper

import com.google.gson.Gson
import com.mango.task.data.model.error.ApiErrorsResponse

fun parseError(errorBody: String?): String {
    val errorResponse = errorBody?.let { errorJson ->
        Gson().fromJson(errorJson, ApiErrorsResponse::class.java)
    }
    return errorResponse?.detail?.joinToString(separator = "\n") { error ->
        "${error.loc.joinToString(" -> ")}: ${error.msg}"
    } ?: "Unknown error"
}