package com.project.collabrix.data.repository

import com.project.collabrix.data.api.UserApi
import com.project.collabrix.data.dto.UserProfile
import com.project.collabrix.data.dto.UserProfileUpdate
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: UserApi
) {
    suspend fun getMyProfile(): UserProfile = api.getMyProfile()
    suspend fun updateMyProfile(update: UserProfileUpdate): UserProfile = api.updateMyProfile(update)
    suspend fun deleteMyAccount() = api.deleteMyAccount()
} 