package com.project.collabrix.data.remote

import com.project.collabrix.data.dto.UserDto
import com.project.collabrix.data.dto.ProjectDto
import retrofit2.http.GET

interface AdminApiService {
    @GET("users")
    suspend fun getAllUsers(): List<UserDto>

    @GET("projects")
    suspend fun getAllProjects(): List<ProjectDto>
} 