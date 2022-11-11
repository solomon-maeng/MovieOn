package com.remember.user.domain

import com.remember.shared.domain.model.DomainEvent

data class RegisteredUserEvent(
    val userId: String,
    val username: String,
    val email: String,
    val token: String,
) : DomainEvent
