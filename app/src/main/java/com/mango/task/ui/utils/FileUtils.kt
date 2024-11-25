package com.mango.task.ui.utils

import android.content.Context
import android.net.Uri
import android.util.Base64

fun convertImageToBase64(context: Context, uri: Uri): String {
    val inputStream = context.contentResolver.openInputStream(uri)
    val bytes = inputStream?.readBytes()
    inputStream?.close()
    return if (bytes != null) {
        Base64.encodeToString(bytes, Base64.NO_WRAP)
    } else {
        ""
    }
}