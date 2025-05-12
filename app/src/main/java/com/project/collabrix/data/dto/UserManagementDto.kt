package com.project.collabrix.data.dto

data class UserManagementDto(
    val id: Int,
    val name: String,
    val email: String,
    val role: String,
    val department: String?,
    val isActive: Boolean
)

enum class UserFilter {
    ALL_USERS,
    STUDENTS,
    PROFESSORS
} 