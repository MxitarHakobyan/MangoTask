package com.mango.task.data.repository

import com.mango.task.data.base.Resources
import com.mango.task.data.localStorage.db.ProfileDao
import com.mango.task.data.model.request.ProfileUpdateRequest
import com.mango.task.data.network.UsersService
import com.mango.task.data.repository.mapper.parseErrors
import com.mango.task.data.repository.mapper.toEntity
import com.mango.task.data.repository.mapper.toProfileState
import com.mango.task.ui.screens.profile.ProfileState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

interface ProfileRepository {
    suspend fun fetchProfile(forceUpdate: Boolean): Flow<Resources<ProfileState>>
    suspend fun updateProfile(profileUpdateRequest: ProfileUpdateRequest): Flow<Resources<Boolean>>
}


class ProfileRepositoryImpl @Inject constructor(
    private val usersService: UsersService,
    private val profileDao: ProfileDao
) : ProfileRepository {

    override suspend fun fetchProfile(forceUpdate: Boolean): Flow<Resources<ProfileState>> {
        return flow {
            emit(Resources.Loading(isLoading = true))

            val localProfile = profileDao.getProfile().firstOrNull()

            if (localProfile != null && !forceUpdate) {
                emit(Resources.Loading(isLoading = false))
                emit(Resources.Success(localProfile.toProfileState()))
            } else {
                try {
                    val response = usersService.fetchProfile()
                    if (response.isSuccessful && response.code() == 200) {
                        val profileEntity = response.body()?.profileData?.toEntity()
                        if (profileEntity != null) {
                            // Insert or update the profile in the database
                            profileDao.insertProfile(profileEntity)
                            emit(Resources.Success(profileEntity.toProfileState()))
                        } else {
                            emit(Resources.Error("No profile data"))
                        }
                    } else {
                        val errorMessage = when (response.code()) {
                            422 -> parseErrors(response.errorBody()?.string())
                            else -> "Unknown error"
                        }
                        emit(Resources.Error(message = errorMessage))
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    emit(Resources.Error(message = "Network error: ${e.message}"))
                } catch (e: HttpException) {
                    e.printStackTrace()
                    emit(Resources.Error(message = "Network error: ${e.message}"))
                } finally {
                    emit(Resources.Loading(isLoading = false))
                }
            }
        }
    }

    override suspend fun updateProfile(profileUpdateRequest: ProfileUpdateRequest): Flow<Resources<Boolean>> {
        return flow {
            emit(Resources.Loading(isLoading = true))

            try {
                val response = usersService.updateProfile(profileData = profileUpdateRequest)

                if (response.isSuccessful && response.code() == 200) {
                    val body = response.body()
                    if (body != null) {
                        val currentProfile = profileDao.getProfile().firstOrNull()
                        if (currentProfile != null) {
                            profileDao.updateProfile(
                                currentProfile.copy(
                                    avatarUrl = body.avatars?.avatar ?: currentProfile.avatarUrl,
                                    miniAvatarUrl = body.avatars?.miniAvatar ?: currentProfile.miniAvatarUrl,
                                    bigAvatarUrl = body.avatars?.bigAvatar ?: currentProfile.bigAvatarUrl,
                                    name = profileUpdateRequest.name,
                                    username = profileUpdateRequest.username,
                                    birthday = profileUpdateRequest.birthday ?: "",
                                    status = profileUpdateRequest.status,
                                    city = profileUpdateRequest.city,
                                    avatar = body.avatars?.avatar ?: currentProfile.avatar,
                                )
                            )
                        }
                        emit(Resources.Success(data = true))
                    } else {
                        emit(Resources.Error(message = "Empty response body"))
                    }
                } else {
                    val errorMessage = when (response.code()) {
                        422 -> parseErrors(response.errorBody()?.string())
                        else -> "Unknown error"
                    }
                    emit(Resources.Error(message = errorMessage))
                }
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resources.Error(message = "Network error: ${e.message}"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resources.Error(message = "Server error: ${e.message}"))
            } finally {
                emit(Resources.Loading(isLoading = false))
            }
        }
    }
}