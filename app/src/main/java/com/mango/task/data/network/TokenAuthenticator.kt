package com.mango.task.data.network

import com.google.gson.Gson
import com.mango.task.data.localStorage.prefs.SecureStorage
import com.mango.task.data.model.response.RefreshTokenResponse
import okhttp3.Authenticator
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val secureStorage: SecureStorage,
    private val gson: Gson
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // Prevent infinite retry loops
        if (response.request.header("Authorization-Retry") != null) {
            return null
        }

        val newAccessToken = refreshAccessToken()

        return if (newAccessToken != null) {
            secureStorage.save(SecureStorage.KEY_ACCESS_TOKEN, newAccessToken)

            response.request.newBuilder()
                .header("Authorization", "Bearer $newAccessToken")
                .header("Authorization-Retry", "true") // Prevent recursive retries
                .build()
        } else {
            null // Return null if token refresh fails, stopping retries
        }
    }

    private fun refreshAccessToken(): String? {
        val refreshToken = secureStorage.get(SecureStorage.KEY_REFRESH_TOKEN) ?: return null

        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("refresh_token", refreshToken)
            .build()

        val request = Request.Builder()
            .url("https://plannerok.ru/api/v1/users/refresh-token")
            .post(requestBody)
            .build()

        return try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val refreshResponse = gson.fromJson(responseBody, RefreshTokenResponse::class.java)
                refreshResponse.accessToken
            } else {
                null // Handle refresh token failure
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null // Handle network or parsing errors
        }
    }
}