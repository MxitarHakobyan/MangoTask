package com.mango.task.data.repository

import com.mango.task.data.localStorage.db.ProfileDao
import com.mango.task.data.model.local.ProfileEntity
import com.mango.task.data.model.request.ProfileUpdateRequest
import com.mango.task.data.network.UsersService
import com.mango.task.data.repository.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val usersService: UsersService,
    private val profileDao: ProfileDao
) {
    fun getProfile(): Flow<ProfileEntity?> {
        return profileDao.getProfile()
    }

    suspend fun fetchProfile(): Result<ProfileEntity> {
        return try {
            val response = usersService.fetchProfile()
            if (response.isSuccessful) {
                response.body()?.profileData?.let { profileData ->
                    val profileEntity = profileData.toEntity()
                    profileDao.insertProfile(profileEntity)
                    Result.success(profileEntity)
                } ?: Result.failure(Exception("No profile data in response"))
            } else {
                Result.failure(Exception("Error fetching profile: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfile(profileUpdateRequest: ProfileUpdateRequest): Result<ProfileEntity> {
        return try {
            val response = usersService.updateProfile(profileUpdateRequest)
            if (response.isSuccessful) {
                response.body()?.profileData?.let { updatedProfile ->
                    val profileEntity = updatedProfile.toEntity()
                    profileDao.insertProfile(profileEntity)
                    Result.success(profileEntity)
                } ?: Result.failure(Exception("No profile data in response"))
            } else {
                Result.failure(Exception("Error updating profile: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}