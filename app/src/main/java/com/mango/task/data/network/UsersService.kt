package com.mango.task.data.network

import com.mango.task.data.model.request.CheckAuthCodeRequest
import com.mango.task.data.model.request.SendAuthCodeRequest
import com.mango.task.data.model.response.CheckAuthCodeResponse
import com.mango.task.data.model.response.SendAuthCodeResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersService {
    @POST("users/send-auth-code/")
    suspend fun sendAuthCode(@Body sendAuthCodeRequest: SendAuthCodeRequest): SendAuthCodeResponse

    @POST("users/check-auth-code/")
    suspend fun checkAuthCode(@Body checkAuthCodeRequest: CheckAuthCodeRequest): CheckAuthCodeResponse
}