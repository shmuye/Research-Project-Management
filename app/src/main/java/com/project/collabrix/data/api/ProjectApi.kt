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

interface ProjectApi {
    @GET("projects/me")
    suspend fun getMyProjects(): List<Project>

    @POST("projects")
    suspend fun createProject(@Body request: CreateProjectRequest): Project

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
}

data class ApplicationStatusUpdate(val status: String) 