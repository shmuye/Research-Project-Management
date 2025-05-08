package com.project.collabrix.data.dto

data class SignupRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String
)

data class SigninRequest(
    val email: String,
    val password: String
)

data class Tokens(
    val accessToken: String,
    val refreshToken: String
)

data class AuthResponse(
    val tokens: Tokens,
    val user: User
)

data class User(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String
) 