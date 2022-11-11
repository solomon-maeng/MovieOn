package com.remember.user.infrastructure.jwt

import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.security.Key

object SecretKeyGenerator {

    fun generate(signingKey: String): Key {
        return Keys.hmacShaKeyFor(signingKey.toByteArray(StandardCharsets.UTF_8))
    }
}
