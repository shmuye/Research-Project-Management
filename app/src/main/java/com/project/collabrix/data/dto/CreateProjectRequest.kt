package com.project.collabrix.data.dto

data class CreateProjectRequest(
    val title: String,
    val description: String,
    val requirements: List<String>,
    val startDate: String,
    val endDate: String,
    val deadline: String
) 