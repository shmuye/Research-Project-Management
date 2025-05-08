package com.project.collabrix.domain.usecase.auth

import com.project.collabrix.domain.model.AuthTokens
import com.project.collabrix.domain.model.User
import com.project.collabrix.domain.repository.AuthRepository
import javax.inject.Inject

class SignupUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        name: String,
        role: String
    ): Result<Pair<AuthTokens, User>> {
        return authRepository.signup(email, password, name, role)
    }
} 