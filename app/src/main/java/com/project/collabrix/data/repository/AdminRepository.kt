package com.project.collabrix.data.repository

import com.project.collabrix.data.dto.UserDto
import com.project.collabrix.data.dto.ProjectDto
import com.project.collabrix.data.remote.AdminApiService
import javax.inject.Inject

class AdminRepository @Inject constructor(private val api: AdminApiService) {
    suspend fun getAllUsers(): List<UserDto> = api.getAllUsers()
    suspend fun getAllProjects(): List<ProjectDto> = api.getAllProjects()

    suspend fun getDashboardStats(): AdminDashboardStats {
        val users = getAllUsers()
        val projects = getAllProjects()
        val totalUsers = users.size
        val totalProfessors = users.count { it.role.equals("PROFESSOR", ignoreCase = true) }
        val totalStudents = users.count { it.role.equals("STUDENT", ignoreCase = true) }
        val recentUsers = users.sortedByDescending { it.id }.take(5)
        val activeProjects = projects.size // Adjust if you have an 'active' field
        return AdminDashboardStats(
            totalUsers = totalUsers,
            totalProfessors = totalProfessors,
            totalStudents = totalStudents,
            activeProjects = activeProjects,
            recentUsers = recentUsers
        )
    }
}

data class AdminDashboardStats(
    val totalUsers: Int,
    val totalProfessors: Int,
    val totalStudents: Int,
    val activeProjects: Int,
    val recentUsers: List<UserDto>
) 