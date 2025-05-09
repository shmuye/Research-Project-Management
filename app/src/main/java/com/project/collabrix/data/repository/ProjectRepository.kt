package com.project.collabrix.data.repository

import com.project.collabrix.data.dto.Project
import com.project.collabrix.data.api.ProjectApi
import com.project.collabrix.data.dto.CreateProjectRequest
import javax.inject.Inject
import com.project.collabrix.data.dto.ProjectDetail
import com.project.collabrix.data.dto.Student
import com.project.collabrix.data.dto.Application
import com.project.collabrix.data.api.ApplicationStatusUpdate

class ProjectRepository @Inject constructor(
    private val api: ProjectApi
) {
    suspend fun getMyProjects(): List<Project> = api.getMyProjects()

    suspend fun createProject(request: CreateProjectRequest): Project = api.createProject(request)

    suspend fun getProjectDetail(projectId: Int): ProjectDetail = api.getProjectDetail(projectId)

    suspend fun deleteProject(projectId: Int) = api.deleteProject(projectId)

    suspend fun removeStudentFromProject(projectId: Int, studentId: Int) =
        api.removeStudentFromProject(projectId, studentId)

    suspend fun getProjectApplications(projectId: Int): List<Application> = api.getProjectApplications(projectId)

    suspend fun updateApplicationStatus(applicationId: Int, status: String): Application =
        api.updateApplicationStatus(applicationId, ApplicationStatusUpdate(status))
} 