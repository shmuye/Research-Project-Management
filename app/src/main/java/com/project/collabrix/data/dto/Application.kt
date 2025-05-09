package com.project.collabrix.data.dto

data class Application(
    val id: Int,
    val studentId: Int,
    val projectId: Int,
    val status: String,
    val student: StudentSummary,
    val projectTitle: String? = null
)

data class StudentSummary(
    val id: Int,
    val name: String?,
    val email: String?
) 