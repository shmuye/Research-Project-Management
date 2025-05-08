package com.project.collabrix.data.repository

import com.project.collabrix.data.model.Project
import com.project.collabrix.data.remote.ProjectApi
import com.project.collabrix.data.dto.CreateProjectRequest
import javax.inject.Inject

class ProjectRepository @Inject constructor(
    private val api: ProjectApi
) {
    suspend fun getMyProjects(): List<Project> = api.getMyProjects()

    suspend fun createProject(request: CreateProjectRequest): Project = api.createProject(request)
} 