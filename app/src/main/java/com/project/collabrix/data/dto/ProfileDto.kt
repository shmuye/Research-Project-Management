package com.project.collabrix.data.dto

data class ProfileDto(
    val id: Int? = null,
    val name: String?,
    val email: String?,
    val department: String?,
    val bio: String?,
    val skills: List<String> = emptyList(),
    val role: String?
) 