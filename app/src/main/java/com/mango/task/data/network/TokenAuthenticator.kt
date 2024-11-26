package com.mango.task.data.network

import com.google.gson.Gson
import com.mango.task.BuildConfig.BASE_URL
import com.mango.task.data.localStorage.prefs.SecureStorage
import com.mango.task.data.model.response.RefreshTokenResponse
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
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

        val newTokensPair = refreshAccessToken()

        return if (newTokensPair?.first != null && newTokensPair.second != null) {
            secureStorage.saveAccessToken(newTokensPair.first!!)
            secureStorage.saveRefreshToken(newTokensPair.second!!)

            response.request.newBuilder()
                .header("Authorization", "Bearer ${newTokensPair.first}")
                .header("Authorization-Retry", "true") // Prevent recursive retries
                .build()
        } else {
            null // Return null if token refresh fails, stopping retries
        }
    }

    private fun refreshAccessToken(): Pair<String?, String?>? {
        val refreshToken = secureStorage.getRefreshToken() ?: return null
        val client = OkHttpClient().newBuilder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()

            requestBuilder.addHeader("Content-Type", "application/json")
            requestBuilder.addHeader("accept", "application/json")

            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()

        val jsonObject = JSONObject()
        jsonObject.put("refresh_token", refreshToken)
        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("${BASE_URL}users/refresh-token/")
            .post(requestBody)
            .build()

        return try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val refreshResponse = gson.fromJson(responseBody, RefreshTokenResponse::class.java)
                Pair(refreshResponse.accessToken, refreshResponse.refreshToken)
            } else {
                null // Handle refresh token failure
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null // Handle network or parsing errors
        }
    }
}