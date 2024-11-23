package com.mango.task.data.repository

import com.mango.task.data.base.Resources
import com.mango.task.data.model.request.CheckAuthCodeRequest
import com.mango.task.data.model.request.SendAuthCodeRequest
import com.mango.task.data.model.response.CheckAuthCodeResponse
import com.mango.task.data.model.response.SendAuthCodeResponse
import com.mango.task.data.network.UsersService
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

            val isSuccess = try {
                usersService.sendAuthCode(sendAuthCodeRequest = sendAuthCodeRequest)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resources.Error(message = "Something went wrong"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resources.Error(message = "Something went wrong"))
                null
            }

            if (isSuccess == null) {
                emit(Resources.Loading(isLoading = false))
            }

            isSuccess.let {
                emit(Resources.Success(data = it))
                emit(Resources.Loading(isLoading = false))
            }
        }
    }

    override suspend fun checkAuthCode(
        checkAuthCodeRequest: CheckAuthCodeRequest
    ): Flow<Resources<CheckAuthCodeResponse>> {
        return flow {
            val checkResult = try {
                usersService.checkAuthCode(checkAuthCodeRequest = checkAuthCodeRequest)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resources.Error(message = "Something went wrong"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resources.Error(message = "Something went wrong"))
                null
            }

            if (checkResult == null) {
                emit(Resources.Loading(isLoading = false))
            }

            checkResult.let {
                emit(Resources.Success(data = it))
                emit(Resources.Loading(isLoading = false))
            }
        }
    }
}