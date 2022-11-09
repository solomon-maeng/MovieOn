package com.remember.shared.contracts

data class RegisterUserCommand(
    val username: String,
    val email: String,
    val password: String
) : Command

data class RegisteredUserConfirmCommand(val token: String, val email: String) : Command

data class LoginUserCommand(val email: String, val password: String) : Command

data class ReIssuanceTokenCommand(val payload: String) : Command
