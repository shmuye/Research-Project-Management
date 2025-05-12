package com.project.collabrix.data.dto

import com.project.collabrix.domain.model.AuthTokens
import com.project.collabrix.domain.model.User

data class SignupRequestDto(
    val email: String,
    val password: String,
    val name: String,
    val role: String
)

data class SigninRequestDto(
    val email: String,
    val password: String
)

data class AuthResponseDto(
    val tokens: AuthTokensDto,
    val user: AuthUserDto
)

data class AuthTokensDto(
    val access_token: String,
    val refresh_token: String
)

data class AuthUserDto(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String
)

// Extension functions to convert DTOs to domain models
fun AuthResponseDto.toDomain(): Pair<AuthTokens, User> {
    return Pair(
        AuthTokens(tokens.access_token, tokens.refresh_token),
        User(user.id, user.email, user.firstName, user.lastName)
    )
}

fun AuthUserDto.toDomain(): User {
    return User(id, email, firstName, lastName)
} 