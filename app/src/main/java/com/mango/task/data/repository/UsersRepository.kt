package com.mango.task.data.repository

import com.mango.task.data.base.Resources
import com.mango.task.data.model.request.CheckAuthCodeRequest
import com.mango.task.data.model.request.SendAuthCodeRequest
import com.mango.task.data.model.response.CheckAuthCodeResponse
import com.mango.task.data.model.response.SendAuthCodeResponse
import com.mango.task.data.network.UsersService
import com.mango.task.data.repository.mapper.parseAuthCodeError
import com.mango.task.data.repository.mapper.parseError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

interface UsersRepository {
    suspend fun sendAuthCode(sendAuthCodeRequest: SendAuthCodeRequest): Flow<Resources<SendAuthCodeResponse>>
    suspend fun checkAuthCode(checkAuthCodeRequest: CheckAuthCodeRequest): Flow<Resources<CheckAuthCodeResponse>>
}

@Singleton
class UsersRepositoryImpl @Inject constructor(
    private val usersService: UsersService
) : UsersRepository {

    override suspend fun sendAuthCode(
        sendAuthCodeRequest: SendAuthCodeRequest
    ): Flow<Resources<SendAuthCodeResponse>> {
        return flow {
            emit(Resources.Loading(isLoading = true))

            val result = try {
                val response = usersService.sendAuthCode(sendAuthCodeRequest = sendAuthCodeRequest)
                if (response.isSuccessful && response.code() == 201) {
                    Resources.Success(response.body())
                } else {
                    when (response.code()) {
                        422 -> Resources.Error(message = parseError(response.errorBody()?.string()))
                        else -> Resources.Error(message = "Unknown error")
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Resources.Error(message = "Network error: ${e.message}")

            } catch (e: HttpException) {
                e.printStackTrace()
                Resources.Error(message = "Network error: ${e.message}")
            }

            emit(Resources.Loading(isLoading = false))
            emit(result)
        }
    }

    override suspend fun checkAuthCode(
        checkAuthCodeRequest: CheckAuthCodeRequest
    ): Flow<Resources<CheckAuthCodeResponse>> {
        return flow {
            emit(Resources.Loading(isLoading = true))

            val result = try {
                val response =
                    usersService.checkAuthCode(checkAuthCodeRequest = checkAuthCodeRequest)
                if (response.isSuccessful && response.code() == 200) {
                    Resources.Success(response.body())
                } else {
                    when (response.code()) {
                        422 -> Resources.Error(message = parseError(response.errorBody()?.string()))
                        404 -> Resources.Error(
                            message = parseAuthCodeError(
                                response.errorBody()?.string()
                            )
                        )

                        else -> Resources.Error(message = "Unknown error")
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Resources.Error(message = "Network error: ${e.message}")

            } catch (e: HttpException) {
                e.printStackTrace()
                Resources.Error(message = "Network error: ${e.message}")
            }

            emit(Resources.Loading(isLoading = false))
            emit(result)
        }
    }
}