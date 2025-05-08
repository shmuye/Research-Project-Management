package com.project.collabrix.data.api

import com.project.collabrix.data.dto.Project
import retrofit2.http.GET
import retrofit2.http.POST
import com.project.collabrix.data.dto.CreateProjectRequest
import retrofit2.http.Body

interface ProjectApi {
    @GET("projects/me")
    suspend fun getMyProjects(): List<Project>

    @POST("projects")
    suspend fun createProject(@Body request: CreateProjectRequest): Project
} 