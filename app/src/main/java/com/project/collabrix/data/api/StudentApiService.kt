package com.project.collabrix.data.api

import andorid.example.collabrix.Model.StudentModel.ActiveProjects
import andorid.example.collabrix.Model.StudentModel.StudentProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface StudentApiService {

    @GET("profile")
    suspend fun getStudentProfile(): Response<StudentProfile>

    @PATCH("profile")
    suspend fun updateStudentProfile(@Body profile: StudentProfile): Response<StudentProfile>

    @GET("projects")
    suspend fun getAllProjects(): Response<List<ActiveProjects>>
}