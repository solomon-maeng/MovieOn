package com.remember.shared

enum class Role {
    USER,
    PRIME_USER,
    ADMIN;

    fun findByRole(role: String) : Role {
        return values().first { r -> r.name == role }
    }
}
