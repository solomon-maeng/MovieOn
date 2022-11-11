package com.remember.integration_test.user.api

import com.remember.shared.Role
import com.remember.support.AbstractApiSpec
import com.remember.user.domain.TokenGenerator
import com.remember.user.domain.User
import com.remember.user.infrastructure.jpa.JpaUserRepository
import com.remember.user.infrastructure.jwt.DefaultTokenParser
import com.remember.user.infrastructure.jwt.RefreshTokenRepository
import com.remember.user.interfaces.TokenResponse
import io.kotest.assertions.assertSoftly
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.transaction.support.TransactionTemplate
import java.util.EnumSet

class POST_ReIssuanceTokenSpec(
    private val generator: TokenGenerator,
    private val parser: DefaultTokenParser,
    private val userRepository: JpaUserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
) : AbstractApiSpec() {

    private lateinit var accessToken: String
    private lateinit var refreshToken: String
    private lateinit var userNotFoundRefreshToken: String
    private lateinit var expiredRefreshToken: String

    init {

        beforeSpec {
            transaction.executeWithoutResult {
                userRepository.save(User.create("rebwon", "rebwon@gmail.com", "123455677!!", "example-token"))
                val userNotFoundRefreshToken = generator.generate("kitty@gmail.com", EnumSet.of(Role.USER))
                val expiredRefreshToken = generator.generate("kitty@gmail.com", EnumSet.of(Role.USER))
                val token = generator.generate("rebwon@gmail.com", EnumSet.of(Role.USER))
                val dbRefreshToken = refreshTokenRepository.findByJti(parser.parse(expiredRefreshToken.refreshToken).jti!!)!!
                dbRefreshToken.expire()

                this.expiredRefreshToken = expiredRefreshToken.refreshToken
                this.userNotFoundRefreshToken = userNotFoundRefreshToken.refreshToken
                this.accessToken = token.accessToken
                this.refreshToken = token.refreshToken
            }
        }

        afterSpec {
            cleaner.clean()
        }

        describe("/api/v1/users/reIssuance") {
            val uri = "http://localhost:$port${REISSUANCE_URI}"

            context("토큰 재발급 요청 시, 유효하지 않은 값이 입력된 경우 400 에러를 반환한다.") {
                withData(
                    Empty(""),
                    Empty("adass"),
                ) { payload ->
                    val request = setUpHeader(payload.value)

                    val response = client.postForEntity(uri, request, TokenResponse::class.java)

                    response.statusCode shouldBe HttpStatus.BAD_REQUEST
                }
            }

            it("토큰 재발급 요청 시, 지원하지 않는 형식의 토큰 값이 들어온 경우 400 에러를 반환한다.") {
                val request = setUpHeader("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJpYXQiOiIxNTg2MzY0MzI3IiwiaXNzIjoiamluaG8uc2hpbiJ9.fWynQLZcHUgeFvFOWT8x-kdRyPmibeMRh4np81Rf9OuXVkbkFCmpdsdbDVWx_QLjdTzAnyBZHPqzKhY1gQDegA")

                val response = client.postForEntity(uri, request, TokenResponse::class.java)

                response.statusCode shouldBe HttpStatus.BAD_REQUEST
            }

            it("토큰 재발급 요청 시, 이미 만료된 토큰인 경우 400 에러를 반환한다.") {
                val request = setUpHeader("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzY29wZXMiOlsiUk9MRV9VU0VSIl0sImVtYWlsIjoibXNvbG8wMjEwMTVAZ21haWwuY29tIiwiaXNzIjoibW92aWVPbiIsImlhdCI6MTY0NjU2MzU4OSwiZXhwIjoxNjQ2OTIzNTg5fQ._xfSkgxVs2Qvamjq-VF5t5T7B5ALgdj-zVPxx692GY0")

                val response = client.postForEntity(uri, request, TokenResponse::class.java)

                response.statusCode shouldBe HttpStatus.BAD_REQUEST
            }

            it("토큰 재발급 요청 시, RefreshToken이 아닌 경우 400 에러를 반환한다.") {
                val request = setUpHeader("Bearer $accessToken")

                val response = client.postForEntity(uri, request, TokenResponse::class.java)

                response.statusCode shouldBe HttpStatus.BAD_REQUEST
            }

            it("토큰 재발급 요청 시, RefreshToken이 이미 만료된 경우 400 에러를 반환한다.") {
                val request = setUpHeader("Bearer $expiredRefreshToken")

                val response = client.postForEntity(uri, request, TokenResponse::class.java)

                response.statusCode shouldBe HttpStatus.BAD_REQUEST
            }

            it("토큰 재발급 요청 시, RefreshToken이 유효하나 유저를 찾을 수 없는 경우 404 에러를 반환한다.") {
                val request = setUpHeader("Bearer $userNotFoundRefreshToken")

                val response = client.postForEntity(uri, request, TokenResponse::class.java)

                response.statusCode shouldBe HttpStatus.NOT_FOUND
            }

            it("토큰 재발급 요청 시, 유효한 입력이 주어지고 재발급된 토큰을 반환한다.") {
                val request = setUpHeader("Bearer $refreshToken")

                val response = client.postForEntity(uri, request, TokenResponse::class.java)

                response.statusCode shouldBe HttpStatus.OK
                assertSoftly(response.body!!) {
                    accessToken shouldNotBe null
                    refreshToken shouldNotBe null
                }
            }
        }
    }

    private fun setUpHeader(value: String): HttpEntity<Any> {
        val headers = HttpHeaders()
        headers[AUTHORIZATION] = value
        return HttpEntity(headers)
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
        private const val REISSUANCE_URI = "/api/v1/users/reIssuance"
    }
}

data class Empty(val value : String)
