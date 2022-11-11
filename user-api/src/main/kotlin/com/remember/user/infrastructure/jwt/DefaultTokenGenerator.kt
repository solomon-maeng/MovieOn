package com.remember.user.infrastructure.jwt

import com.remember.shared.Role
import com.remember.user.domain.Token
import com.remember.user.domain.TokenGenerator
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey

class DefaultTokenGenerator(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val tokenProperties: TokenProperties,
) : TokenGenerator {
    override fun generate(email: String, roles: Set<Role>): Token {
        return Token(generateAccessToken(email, roles), generateRefreshToken(email))
    }

    private fun generateRefreshToken(email: String): String {
        val claims = generateClaims(email, listOf(Scopes.REFRESH_TOKEN.authority()))
        val currentTime = LocalDateTime.now()
        val randomJti = UUID.randomUUID().toString()
        refreshTokenRepository.save(RefreshToken(randomJti))
        val key = generateKey()

        return Jwts.builder()
            .setClaims(claims)
            .setId(randomJti)
            .setIssuer(tokenProperties.tokenIssuer)
            .setIssuedAt(Date.from(toInstant(currentTime)))
            .setExpiration(Date.from(toInstant(currentTime.plusMinutes(tokenProperties.refreshExpirationSec.toLong()))))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    private fun generateAccessToken(email: String, roles: Set<Role>): String {
        val claims = generateClaims(email, authorities(roles))
        val currentTime = LocalDateTime.now()
        val key = generateKey()

        return Jwts.builder().setClaims(claims)
            .setIssuedAt(Date.from(toInstant(currentTime)))
            .setExpiration(Date.from(toInstant(currentTime.plusMinutes(tokenProperties.tokenExpirationSec.toLong()))))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    private fun generateClaims(email: String, scopes: List<String>): MutableMap<String, Any> {
        val claims = mutableMapOf<String, Any>()
        claims["email"] = email
        claims["scopes"] = scopes
        return claims
    }

    private fun generateKey(): SecretKey? =
        Keys.hmacShaKeyFor(tokenProperties.base64TokenSigningKey.toByteArray(StandardCharsets.UTF_8))

    private fun toInstant(currentTime: LocalDateTime): Instant? =
        currentTime.atZone(ZoneId.systemDefault()).toInstant()

    private fun authorities(roles: Set<Role>): List<String> {
        return roles.asSequence().map { "ROLE_" + it.name }.toList()
    }
}
