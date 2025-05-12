package com.project.collabrix.data.repository

import com.project.collabrix.data.api.UserManagementApiService
import com.project.collabrix.data.dto.UserManagementDto
import javax.inject.Inject

class UserManagementRepository @Inject constructor(
    private val api: UserManagementApiService
) {
    suspend fun getAllUsers(): List<UserManagementDto> = api.getAllUsers()
    
    suspend fun getStudents(): List<UserManagementDto> = api.getStudents()
    
    suspend fun getProfessors(): List<UserManagementDto> = api.getProfessors()
    
    suspend fun deleteUser(userId: Int): Result<Unit> = try {
        val response = api.deleteUser(userId)
        if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Failed to delete user: ${response.code()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
} 