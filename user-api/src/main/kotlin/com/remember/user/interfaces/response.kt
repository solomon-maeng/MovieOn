package com.remember.user.interfaces

import java.time.LocalDateTime

data class TokenResponse(val accessToken: String, val refreshToken: String)

data class UserResponse(
    val userId: String,
    val username: String,
    val email: String,
    val verified: Boolean,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)
