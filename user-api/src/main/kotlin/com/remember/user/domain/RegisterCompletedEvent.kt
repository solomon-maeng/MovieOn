package com.remember.user.domain

import com.remember.shared.domain.model.DomainEvent

data class RegisterCompletedEvent(val userKey: String, val email: String) : DomainEvent
