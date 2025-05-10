package com.project.collabrix.data.api

import com.project.collabrix.data.dto.Project
import retrofit2.http.GET
import retrofit2.http.POST
import com.project.collabrix.data.dto.CreateProjectRequest
import retrofit2.http.Body
import com.project.collabrix.data.dto.ProjectDetail
import com.project.collabrix.data.dto.Student
import retrofit2.http.DELETE
import retrofit2.http.Path
import com.project.collabrix.data.dto.Application
import retrofit2.http.PATCH
import retrofit2.http.Query
import com.project.collabrix.data.dto.ProfileDto
import com.project.collabrix.data.dto.ApplyToProjectRequest
import com.project.collabrix.data.dto.ProjectSummary

interface ProjectApi {
    @GET("projects/me")
    suspend fun getMyProjects(): List<ProjectSummary>

    @POST("projects")
    suspend fun createProject(@Body request: CreateProjectRequest): ProjectSummary

    @GET("projects/{id}")
    suspend fun getProjectDetail(@Path("id") projectId: Int): ProjectDetail

    @DELETE("projects/me/{id}")
    suspend fun deleteProject(@Path("id") projectId: Int)

    @DELETE("projects/{projectId}/students/{studentId}")
    suspend fun removeStudentFromProject(
        @Path("projectId") projectId: Int,
        @Path("studentId") studentId: Int
    )

    @GET("applications/project/{projectId}")
    suspend fun getProjectApplications(@Path("projectId") projectId: Int): List<Application>

    @PATCH("applications/{applicationId}")
    suspend fun updateApplicationStatus(
        @Path("applicationId") applicationId: Int,
        @Body statusUpdate: ApplicationStatusUpdate
    ): Application

    @GET("applications")
    suspend fun getStudentApplications(): List<Application>

    @GET("profile")
    suspend fun getProfile(): ProfileDto

    @GET("projects")
    suspend fun getAllProjects(): List<ProjectSummary>

    @POST("applications")
    suspend fun applyToProject(@Body request: ApplyToProjectRequest): Application
}

data class ApplicationStatusUpdate(val status: String) 