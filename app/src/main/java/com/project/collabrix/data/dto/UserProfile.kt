package com.project.collabrix.data.dto

data class UserProfile(
    val id: Int,
    val name: String?,
    val email: String,
    val department: String?,
    val bio: String?,
    val role: String,
    val researchInterests: String?
) {
    fun getResearchInterestsList(): List<String> =
        researchInterests?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList()
}

data class UserProfileUpdate(
    val name: String?,
    val department: String?,
    val bio: String?,
    val researchInterests: String?
) 