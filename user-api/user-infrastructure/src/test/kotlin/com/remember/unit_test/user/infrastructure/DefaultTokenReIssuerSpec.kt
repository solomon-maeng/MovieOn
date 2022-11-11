package com.remember.unit_test.user.infrastructure

import com.remember.shared.Role
import com.remember.user.domain.Token
import com.remember.user.domain.UserNotFound
import com.remember.user.domain.UserRepository
import com.remember.user.infrastructure.jwt.AlreadyExpiredToken
import com.remember.user.infrastructure.jwt.DefaultTokenGenerator
import com.remember.user.infrastructure.jwt.DefaultTokenParser
import com.remember.user.infrastructure.jwt.DefaultTokenReIssuer
import com.remember.user.infrastructure.jwt.InvalidArgument
import com.remember.user.infrastructure.jwt.InvalidToken
import com.remember.user.infrastructure.jwt.RefreshToken
import com.remember.user.infrastructure.jwt.RefreshTokenNotFound
import com.remember.user.infrastructure.jwt.RefreshTokenRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.EnumSet
import java.util.UUID

class DefaultTokenReIssuerSpec : StringSpec({

    "토큰 재발급 시, payload가 Blank인 경우 예외가 발생한다." {
        val refreshTokenRepository = mockk<RefreshTokenRepository>()
        val sut = DefaultTokenReIssuer(
            DefaultTokenGenerator(refreshTokenRepository, tokenProperties()),
            DefaultTokenParser(tokenProperties()),
            refreshTokenRepository,
            mockk()
        )

        val result = shouldThrow<InvalidArgument> { sut.reIssuance("") }

        result.message shouldBe "잘못된 입력입니다."
    }

    "토큰 재발급 시, 토큰이 Refresh 타입이 아닌 경우 예외가 발생한다." {
        val refreshTokenRepository = mockk<RefreshTokenRepository>()
        every { refreshTokenRepository.save(any()) } returns mockk()

        val generator = DefaultTokenGenerator(refreshTokenRepository, tokenProperties())
        val token = getToken(generator).accessToken
        val sut = DefaultTokenReIssuer(
            generator, DefaultTokenParser(tokenProperties()), refreshTokenRepository, mockk()
        )

        val result = shouldThrow<InvalidToken> { sut.reIssuance(PREFIX + token) }

        result.message shouldBe "RefreshToken이 아닙니다."
        verify(exactly = 1) { refreshTokenRepository.save(any()) }
    }

    "토큰 재발급 시, jti에 맞는 RefreshToken을 찾을 수 없는 경우 예외가 발생한다." {
        val refreshTokenRepository = mockk<RefreshTokenRepository>()
        every { refreshTokenRepository.save(any()) } returns mockk()
        every { refreshTokenRepository.findByJti(any()) } returns null

        val generator = DefaultTokenGenerator(refreshTokenRepository, tokenProperties())
        val token = getToken(generator).refreshToken
        val sut = DefaultTokenReIssuer(
            generator, DefaultTokenParser(tokenProperties()), refreshTokenRepository, mockk()
        )

        val result = shouldThrow<RefreshTokenNotFound> { sut.reIssuance(PREFIX + token) }

        result.message shouldBe "RefreshToken을 찾을 수 없습니다."
        verify(exactly = 1) { refreshTokenRepository.save(any()) }
        verify(exactly = 1) { refreshTokenRepository.findByJti(any()) }
    }

    "토큰 재발급 시, RefreshToken이 이미 만료된 경우 예외가 발생한다." {
        val refreshTokenRepository = mockk<RefreshTokenRepository>()
        val expiredRefreshToken = setUpAlreadyExpiredRefreshToken()
        every { refreshTokenRepository.save(any()) } returns mockk()
        every { refreshTokenRepository.findByJti(any()) } returns expiredRefreshToken

        val generator = DefaultTokenGenerator(refreshTokenRepository, tokenProperties())
        val token = getToken(generator).refreshToken
        val sut = DefaultTokenReIssuer(
            generator, DefaultTokenParser(tokenProperties()), refreshTokenRepository, mockk()
        )

        val result = shouldThrow<AlreadyExpiredToken> { sut.reIssuance(PREFIX + token) }

        result.message shouldBe "이미 만료된 토큰입니다."
        verify(exactly = 1) { refreshTokenRepository.save(any()) }
        verify(exactly = 1) { refreshTokenRepository.findByJti(any()) }
    }

    "토큰 재발급 시, RefreshToken을 통해 유저를 조회할 수 없는 경우 예외가 발생한다." {
        val refreshTokenRepository = mockk<RefreshTokenRepository>()
        val userRepository = mockk<UserRepository>()
        every { refreshTokenRepository.save(any()) } returns mockk()
        every { refreshTokenRepository.findByJti(any()) } returns RefreshToken(UUID.randomUUID().toString())
        every { userRepository.findByEmail(any()) } returns null

        val generator = DefaultTokenGenerator(refreshTokenRepository, tokenProperties())
        val token = getToken(generator).refreshToken
        val sut = DefaultTokenReIssuer(
            generator, DefaultTokenParser(tokenProperties()), refreshTokenRepository, userRepository
        )

        val result = shouldThrow<UserNotFound> { sut.reIssuance(PREFIX + token) }

        result.message shouldBe "유저가 존재하지 않습니다."
        verify(exactly = 1) { refreshTokenRepository.save(any()) }
        verify(exactly = 1) { refreshTokenRepository.findByJti(any()) }
        verify(exactly = 1) { userRepository.findByEmail(any()) }
    }
})

private fun setUpAlreadyExpiredRefreshToken(): RefreshToken {
    val refreshToken = RefreshToken(UUID.randomUUID().toString())
    refreshToken.expire()
    return refreshToken
}

private fun getToken(generator: DefaultTokenGenerator): Token {
    return generator.generate("kitty@gmail.com", EnumSet.of(Role.USER))
}

private const val PREFIX = "Bearer "
