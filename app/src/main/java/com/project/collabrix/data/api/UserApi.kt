package com.project.collabrix.data.api

import com.project.collabrix.data.dto.UserProfile
import com.project.collabrix.data.dto.UserProfileUpdate
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserApi {
    @GET("users/me")
    suspend fun getMyProfile(): UserProfile

    @PATCH("users/me")
    suspend fun updateMyProfile(@Body update: UserProfileUpdate): UserProfile

    @DELETE("users/me")
    suspend fun deleteMyAccount()
} 