package com.remember.user.domain

import com.remember.shared.Role

interface TokenGenerator {

    fun generate(email: String, roles: Set<Role>): Token
}
