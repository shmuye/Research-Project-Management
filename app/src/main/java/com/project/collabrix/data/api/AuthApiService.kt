package com.project.collabrix.data.api

import com.project.collabrix.data.dto.AuthResponseDto
import com.project.collabrix.data.dto.SigninRequestDto
import com.project.collabrix.data.dto.SignupRequestDto
import com.project.collabrix.data.dto.AuthTokensDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/signup")
    suspend fun signup(@Body request: SignupRequestDto): Response<AuthTokensDto>

    @POST("auth/signin")
    suspend fun signin(@Body request: SigninRequestDto): Response<AuthTokensDto>

    @POST("auth/logout")
    suspend fun logout(): Response<Unit>

    @POST("auth/refresh")
    suspend fun refreshTokens(): Response<AuthTokensDto>
} 