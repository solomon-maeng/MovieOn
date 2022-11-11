package com.remember.user.infrastructure.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "app.security.jwt")
@ConstructorBinding
data class TokenProperties(
    val tokenExpirationSec: Int,
    val tokenIssuer: String,
    val base64TokenSigningKey: String,
    val refreshExpirationSec: Int
)
