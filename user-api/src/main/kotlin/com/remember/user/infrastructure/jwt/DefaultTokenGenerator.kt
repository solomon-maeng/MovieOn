package com.remember.user.infrastructure.jwt

import com.remember.shared.Role
import com.remember.user.domain.Token
import com.remember.user.domain.TokenGenerator

class DefaultTokenGenerator : TokenGenerator {
    override fun generate(email: String, roles: Set<Role>): Token {
        TODO("Not yet implemented")
    }
}
