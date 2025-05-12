package com.project.collabrix.data.api

import com.project.collabrix.data.dto.UserManagementDto
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface UserManagementApiService {
    @GET("admin/users")
    suspend fun getAllUsers(): List<UserManagementDto>

    @GET("admin/users/students")
    suspend fun getStudents(): List<UserManagementDto>

    @GET("admin/users/professors")
    suspend fun getProfessors(): List<UserManagementDto>

    @DELETE("admin/users/{userId}")
    suspend fun deleteUser(@Path("userId") userId: Int): Response<Unit>
} 