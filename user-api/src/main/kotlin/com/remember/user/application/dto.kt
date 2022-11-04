package com.remember.user.application

import java.time.LocalDate
import java.time.LocalDateTime

data class UserDto(
    val userKey: String,
    val username: String,
    val email: String,
    val verified: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDate
)

data class TokenDto(val accessToken: String, val refreshToken: String)
