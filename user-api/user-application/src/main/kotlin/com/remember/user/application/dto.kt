package com.remember.user.application

import java.time.LocalDateTime

data class UserDto(
    val userId: String,
    val username: String,
    val email: String,
    val verified: Boolean,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)

data class TokenDto(val accessToken: String, val refreshToken: String)
