package com.remember.user.infrastructure.jwt

import com.remember.user.domain.Token
import com.remember.user.domain.TokenGenerator
import com.remember.user.domain.TokenReIssuer
import com.remember.user.domain.UserNotFound
import com.remember.user.domain.UserRepository

class DefaultTokenReIssuer(
    private val generator: TokenGenerator,
    private val parser: DefaultTokenParser,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userRepository: UserRepository,
) : TokenReIssuer {

    override fun reIssuance(payload: String): Token {
        val token = parseRawToken(payload)
        token.validate(Scopes.REFRESH_TOKEN.authority())

        val refreshToken = token.jti?.let { refreshTokenRepository.findByJti(it) } ?: throw RefreshTokenNotFound()
        refreshToken.expire()

        val user = userRepository.findByEmail(token.subject) ?: throw UserNotFound()
        return generator.generate(user.email, user.roles)
    }

    private fun parseRawToken(payload: String): RawToken {
        val rawToken = DefaultTokenExtractor.extract(payload) ?: throw InvalidArgument()
        return parser.parse(rawToken)
    }
}
