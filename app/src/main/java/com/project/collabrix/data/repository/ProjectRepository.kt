package com.project.collabrix.data.repository

import com.project.collabrix.data.dto.ProjectSummary
import com.project.collabrix.data.api.ProjectApi
import com.project.collabrix.data.dto.CreateProjectRequest
import javax.inject.Inject
import com.project.collabrix.data.dto.ProjectDetail
import com.project.collabrix.data.dto.Student
import com.project.collabrix.data.dto.Application
import com.project.collabrix.data.api.ApplicationStatusUpdate
import com.project.collabrix.data.dto.ProfileDto
import com.project.collabrix.data.dto.ApplyToProjectRequest

class ProjectRepository @Inject constructor(
    private val api: ProjectApi
) {
    suspend fun getMyProjects(): List<ProjectSummary> = api.getMyProjects()

    suspend fun getAllProjects(): List<ProjectSummary> = api.getAllProjects()

    suspend fun createProject(request: CreateProjectRequest): ProjectSummary = api.createProject(request)

    suspend fun getProjectDetail(projectId: Int): ProjectDetail = api.getProjectDetail(projectId)

    suspend fun deleteProject(projectId: Int) = api.deleteProject(projectId)

    suspend fun removeStudentFromProject(projectId: Int, studentId: Int) =
        api.removeStudentFromProject(projectId, studentId)

    suspend fun getProjectApplications(projectId: Int): List<Application> = api.getProjectApplications(projectId)

    suspend fun updateApplicationStatus(applicationId: Int, status: String): Application =
        api.updateApplicationStatus(applicationId, ApplicationStatusUpdate(status))

    suspend fun getStudentApplications(): List<Application> = api.getStudentApplications()

    suspend fun getProfile(): ProfileDto = api.getProfile()

    suspend fun applyToProject(projectId: Int): Application =
        api.applyToProject(ApplyToProjectRequest(projectId))
} 