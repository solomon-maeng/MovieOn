package com.remember.user.infrastructure.jwt

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import java.lang.IllegalArgumentException
import java.lang.RuntimeException

class DefaultTokenParser(
    private val tokenProperties: TokenProperties
) {

    fun parse(token: String): RawToken {
        try {
            val jwt = Jwts.parserBuilder()
                .setSigningKey(SecretKeyGenerator.generate(tokenProperties.base64TokenSigningKey))
                .build()
                .parseClaimsJws(token)
            return RawToken(
                jwt.body["email"].toString(),
                jwt.body["jti"] as? String,
                jwt.body["scopes"] as List<String>
            )
        } catch (ex: Exception) {
            when(ex) {
                is UnsupportedJwtException -> throw InvalidToken("지원하지 않는 JWT 토큰입니다.")
                is MalformedJwtException -> throw InvalidToken("잘못된 JWT 토큰입니다.")
                is IllegalArgumentException -> throw InvalidToken("입력된 토큰이 잘못되었습니다.")
                is ExpiredJwtException -> throw AlreadyExpiredToken()
                else -> throw RuntimeException("알 수 없는 에러입니다.")
            }
        }
    }
}
