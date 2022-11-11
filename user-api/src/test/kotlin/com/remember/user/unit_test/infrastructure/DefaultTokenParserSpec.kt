package com.remember.user.unit_test.infrastructure

import com.remember.shared.Role
import com.remember.user.infrastructure.jwt.DefaultTokenGenerator
import com.remember.user.infrastructure.jwt.DefaultTokenParser
import com.remember.user.infrastructure.jwt.InvalidToken
import com.remember.user.infrastructure.jwt.RefreshTokenRepository
import com.remember.user.infrastructure.jwt.Scopes
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import java.util.EnumSet

class DefaultTokenParserSpec : StringSpec({

    "AccessToken을 파싱하면, jti가 null이고 verify 검증 시 예외가 발생한다." {
        val refreshTokenRepository = mockk<RefreshTokenRepository>()
        every { refreshTokenRepository.save(any()) } returns mockk()
        val generator = DefaultTokenGenerator(refreshTokenRepository, tokenProperties())
        val token = getToken(generator).accessToken
        val sut = DefaultTokenParser(tokenProperties())

        val rawToken = sut.parse(token)
        val result = shouldThrow<InvalidToken> { rawToken.validate(Scopes.REFRESH_TOKEN.authority()) }

        assertSoftly(rawToken) {
            subject shouldBe "kitty@gmail.com"
            authorities shouldBe listOf("ROLE_USER")
            jti shouldBe null
        }
        result.message shouldBe "RefreshToken이 아닙니다."
    }

    "RefreshToken을 파싱하면, jti가 not-null이고 verify 검증 시 예외가 발생하지 않는다." {
        val refreshTokenRepository = mockk<RefreshTokenRepository>()
        every { refreshTokenRepository.save(any()) } returns mockk()
        val generator = DefaultTokenGenerator(refreshTokenRepository, tokenProperties())
        val token = getToken(generator).refreshToken
        val sut = DefaultTokenParser(tokenProperties())

        val rawToken = sut.parse(token)
        shouldNotThrow<InvalidToken> { rawToken.validate(Scopes.REFRESH_TOKEN.authority()) }

        assertSoftly(rawToken) {
            subject shouldBe "kitty@gmail.com"
            authorities shouldBe listOf("ROLE_REFRESH_TOKEN")
            jti shouldNotBe null
        }
    }

    "잘못된 토큰을 파싱한 경우, 예외가 발생한다." {
        val sut = DefaultTokenParser(tokenProperties())

        shouldThrow<InvalidToken> { sut.parse("invalid_token") }
    }
})

private fun getToken(generator: DefaultTokenGenerator) =
    generator.generate("kitty@gmail.com", EnumSet.of(Role.USER))
