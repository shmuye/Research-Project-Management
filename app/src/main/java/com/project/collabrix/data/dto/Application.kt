package com.project.collabrix.data.dto

data class Application(
    val id: Int,
    val studentId: Int,
    val projectId: Int,
    val status: String,
    val student: StudentSummary?,
    val projectTitle: String? = null,
    val project: ProjectSummary? = null
)

data class StudentSummary(
    val id: Int,
    val name: String?,
    val email: String?
)

data class ProjectSummary(
    val id: Int,
    val title: String?,
    val description: String?,
    val status: String?,
    val professorName: String?,
    val department: String?,
    val deadline: String?
)

data class ApplyToProjectRequest(
    val projectId: Int
) 