package com.remember.integration_test.user.query

import com.remember.support.IntegrationSpecHelper
import com.remember.user.infrastructure.jwt.RefreshToken
import com.remember.user.infrastructure.jwt.RefreshTokenRepository
import io.kotest.matchers.shouldBe
import java.util.UUID

class RefreshTokenRepositorySpec(
    private val refreshTokenRepository: RefreshTokenRepository,
) : IntegrationSpecHelper() {

    init {
        lateinit var expected: RefreshToken

        beforeEach {
            expected = transaction.execute {
                refreshTokenRepository.save(RefreshToken(UUID.randomUUID().toString()))
            }!!
        }

        afterEach {
            cleaner.clean()
        }

        describe("findByJti 검증") {
            it("Jti와 일치하는 한 건의 RefreshToken을 반환한다.") {
                val actual = refreshTokenRepository.findByJti(expected.jti)
                expected shouldBe actual
            }
        }
    }
}
