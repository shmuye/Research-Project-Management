package com.project.collabrix.data.dto

data class UserProfile(
    val id: Int,
    val name: String?,
    val email: String,
    val department: String?,
    val bio: String?,
    val role: String
)

data class UserProfileUpdate(
    val name: String?,
    val department: String?,
    val bio: String?
) 