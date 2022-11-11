package com.remember.unit_test.user.infrastructure

import com.remember.shared.Role
import com.remember.user.infrastructure.jwt.AlreadyExpiredToken
import com.remember.user.infrastructure.jwt.DefaultTokenGenerator
import com.remember.user.infrastructure.jwt.DefaultTokenParser
import com.remember.user.infrastructure.jwt.InvalidToken
import com.remember.user.infrastructure.jwt.RefreshTokenRepository
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import java.util.EnumSet

class DefaultTokenParserSpec : StringSpec({

    "AccessToken을 파싱하면, jti는 null이고 user 역할을 가진 인증 정보가 반환된다." {
        val refreshTokenRepository = mockk<RefreshTokenRepository>()
        every { refreshTokenRepository.save(any()) } returns mockk()
        val generator = DefaultTokenGenerator(refreshTokenRepository, tokenProperties())
        val token = getToken(generator).accessToken
        val sut = DefaultTokenParser(tokenProperties())

        val rawToken = sut.parse(token)

        assertSoftly(rawToken) {
            subject shouldBe "kitty@gmail.com"
            authorities shouldBe listOf("ROLE_USER")
            jti shouldBe null
        }
    }

    "RefreshToken을 파싱하면, jti가 not-null이고 refresh token 역할을 가진 인증 정보가 반환된다." {
        val refreshTokenRepository = mockk<RefreshTokenRepository>()
        every { refreshTokenRepository.save(any()) } returns mockk()
        val generator = DefaultTokenGenerator(refreshTokenRepository, tokenProperties())
        val token = getToken(generator).refreshToken
        val sut = DefaultTokenParser(tokenProperties())

        val rawToken = sut.parse(token)

        assertSoftly(rawToken) {
            subject shouldBe "kitty@gmail.com"
            authorities shouldBe listOf("ROLE_REFRESH_TOKEN")
            jti shouldNotBe null
        }
    }

    "잘못된 토큰을 파싱한 경우, 예외가 발생한다." {
        val sut = DefaultTokenParser(tokenProperties())

        val result = shouldThrow<InvalidToken> { sut.parse("invalid_token") }

        result.message shouldBe "잘못된 JWT 토큰입니다."
    }

    "지원하지 않는 토큰 파싱한 경우, 예외가 발생한다." {
        val sut = DefaultTokenParser(tokenProperties())

        val result =
            shouldThrow<InvalidToken> { sut.parse("eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJpYXQiOiIxNTg2MzY0MzI3IiwiaXNzIjoiamluaG8uc2hpbiJ9.fWynQLZcHUgeFvFOWT8x-kdRyPmibeMRh4np81Rf9OuXVkbkFCmpdsdbDVWx_QLjdTzAnyBZHPqzKhY1gQDegA") }

        result.message shouldBe "지원하지 않는 JWT 토큰입니다."
    }

    "이미 만료된 경우, 예외가 발생한다." {
        val sut = DefaultTokenParser(tokenProperties())

        val result =
            shouldThrow<AlreadyExpiredToken> { sut.parse("eyJhbGciOiJIUzI1NiJ9.eyJzY29wZXMiOlsiUk9MRV9VU0VSIl0sImVtYWlsIjoibXNvbG8wMjEwMTVAZ21haWwuY29tIiwiaXNzIjoibW92aWVPbiIsImlhdCI6MTY0NjU2MzU4OSwiZXhwIjoxNjQ2OTIzNTg5fQ._xfSkgxVs2Qvamjq-VF5t5T7B5ALgdj-zVPxx692GY0") }

        result.message shouldBe "이미 만료된 토큰입니다."
    }
})

private fun getToken(generator: DefaultTokenGenerator) =
    generator.generate("kitty@gmail.com", EnumSet.of(Role.USER))
