package com.mango.task.data.repository.mapper

import com.google.gson.Gson
import com.mango.task.data.model.error.CheckAuthCodeErrorResponse

fun parseAuthCodeError(errorBody: String?): String {
    val errorResponse = errorBody?.let { errorJson ->
        Gson().fromJson(errorJson, CheckAuthCodeErrorResponse::class.java)
    }
    return errorResponse?.detail?.message ?: "Unknown error"
}