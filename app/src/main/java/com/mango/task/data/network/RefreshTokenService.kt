package com.mango.task.data.network

import com.mango.task.data.model.response.RefreshTokenResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface RefreshTokenService {
    @POST("users/refresh-token")
    suspend fun refreshAccessToken(@Query("refresh_token") refreshToken: String): Response<RefreshTokenResponse>
}
