package com.mango.task.data.network

import com.mango.task.data.model.request.CheckAuthCodeRequest
import com.mango.task.data.model.request.ProfileUpdateRequest
import com.mango.task.data.model.request.RegistrationRequest
import com.mango.task.data.model.request.SendAuthCodeRequest
import com.mango.task.data.model.response.CheckAuthCodeResponse
import com.mango.task.data.model.response.ProfileResponse
import com.mango.task.data.model.response.RegistrationResponse
import com.mango.task.data.model.response.SendAuthCodeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface UsersService {
    @POST("users/send-auth-code/")
    suspend fun sendAuthCode(@Body sendAuthCodeRequest: SendAuthCodeRequest): Response<SendAuthCodeResponse>

    @POST("users/check-auth-code/")
    suspend fun checkAuthCode(@Body checkAuthCodeRequest: CheckAuthCodeRequest): Response<CheckAuthCodeResponse>

    @POST("users/register/")
    suspend fun registration(@Body registrationRequest: RegistrationRequest): Response<RegistrationResponse>

    @GET("users/me/")
    suspend fun fetchProfile(): Response<ProfileResponse>

    @PUT("users/me")
    suspend fun updateProfile(@Body profileData: ProfileUpdateRequest): Response<ProfileResponse>
}