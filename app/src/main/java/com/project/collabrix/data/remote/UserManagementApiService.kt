package com.project.collabrix.data.remote

import com.project.collabrix.data.dto.UserManagementDto
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface UserManagementApiService {
    @GET("users")
    suspend fun getAllUsers(): List<UserManagementDto>

    @GET("users/role/students")
    suspend fun getStudents(): List<UserManagementDto>

    @GET("users/role/professors")
    suspend fun getProfessors(): List<UserManagementDto>

    @DELETE("users/{userId}")
    suspend fun deleteUser(@Path("userId") userId: Int): Response<Unit>
} 