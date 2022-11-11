package com.remember.integration_test.user.query

import com.remember.support.AbstractRepositorySpec
import com.remember.support.DatabaseCleaner
import com.remember.user.infrastructure.jwt.RefreshToken
import com.remember.user.infrastructure.jwt.RefreshTokenRepository
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import java.util.UUID

class RefreshTokenRepositorySpec(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val cleaner: DatabaseCleaner,
) : AbstractRepositorySpec() {

    init {
        lateinit var expected: RefreshToken

        beforeEach {
            expected = refreshTokenRepository.save(RefreshToken(UUID.randomUUID().toString()))
        }

        afterEach {
            cleaner.clean()
        }

        describe("existsByJti 검증") {
            context("Jti가 존재하지 않는다면") {
                it("false를 반환한다.") {
                    refreshTokenRepository.existsByJti("example").shouldBeFalse()
                }
            }

            context("Jti가 존재한다면") {
                it("true를 반환한다.") {
                    refreshTokenRepository.existsByJti(expected.jti).shouldBeTrue()
                }
            }
        }

        describe("findByJti 검증") {
            it("Jti와 일치하는 한 건의 RefreshToken을 반환한다.") {
                val actual = refreshTokenRepository.findByJti(expected.jti)
                expected shouldBe actual
            }
        }
    }
}
