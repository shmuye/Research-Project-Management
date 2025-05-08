package com.project.collabrix.domain.repository

import com.project.collabrix.domain.model.AuthTokens
import com.project.collabrix.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signup(email: String, password: String, name: String, role: String): Result<Pair<AuthTokens, User>>
    suspend fun signin(email: String, password: String): Result<Pair<AuthTokens, User>>
    suspend fun logout(): Result<Unit>
    suspend fun refreshTokens(): Result<AuthTokens>
    fun getCurrentUser(): Flow<User?>
    suspend fun saveTokens(tokens: AuthTokens)
    suspend fun clearTokens()
} 