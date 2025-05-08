package com.project.collabrix.data.repository

import com.project.collabrix.data.api.AuthApiService
import com.project.collabrix.data.dto.SigninRequestDto
import com.project.collabrix.data.dto.SignupRequestDto
import com.project.collabrix.data.local.UserPreferences
import com.project.collabrix.domain.model.AuthTokens
import com.project.collabrix.domain.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val userPreferences: UserPreferences
) : AuthRepository {

    override suspend fun signup(
        email: String,
        password: String,
        name: String,
        role: String
    ): Result<Pair<AuthTokens, User>> {
        return try {
            val response = authApiService.signup(
                SignupRequestDto(email, password, name, role)
            )
            if (response.isSuccessful) {
                response.body()?.let { tokens ->
                    val authTokens = AuthTokens(tokens.access_token, tokens.refresh_token)
                    saveTokens(authTokens)
                    // Create a temporary user object since we don't get user data from signup
                    val user = User(0, email, name, "") // We'll get the real user data on signin
                    userPreferences.saveUser(user)
                    Result.success(Pair(authTokens, user))
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Signup failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signin(email: String, password: String): Result<Pair<AuthTokens, User>> {
        return try {
            val response = authApiService.signin(SigninRequestDto(email, password))
            if (response.isSuccessful) {
                response.body()?.let { tokens ->
                    val authTokens = AuthTokens(tokens.access_token, tokens.refresh_token)
                    saveTokens(authTokens)
                    // Create a temporary user object since we don't get user data from signin
                    val user = User(0, email, "", "") // We'll get the real user data later
                    userPreferences.saveUser(user)
                    Result.success(Pair(authTokens, user))
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Signin failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            val response = authApiService.logout()
            if (response.isSuccessful) {
                clearTokens()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Logout failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun refreshTokens(): Result<AuthTokens> {
        return try {
            val response = authApiService.refreshTokens()
            if (response.isSuccessful) {
                response.body()?.let { tokens ->
                    val authTokens = AuthTokens(tokens.access_token, tokens.refresh_token)
                    saveTokens(authTokens)
                    Result.success(authTokens)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Token refresh failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): Flow<User?> = userPreferences.getCurrentUser()

    override suspend fun saveTokens(tokens: AuthTokens) {
        userPreferences.saveTokens(tokens)
    }

    override suspend fun clearTokens() {
        userPreferences.clearTokens()
    }
}