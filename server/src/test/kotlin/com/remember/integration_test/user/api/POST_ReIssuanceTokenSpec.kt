package com.remember.integration_test.user.api

import com.remember.shared.Role
import com.remember.support.IntegrationSpecHelper
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
import java.util.EnumSet

class POST_ReIssuanceTokenSpec(
    private val generator: TokenGenerator,
    private val parser: DefaultTokenParser,
    private val userRepository: JpaUserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
) : IntegrationSpecHelper() {

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

            context("?????? ????????? ?????? ???, ???????????? ?????? ?????? ????????? ?????? 400 ????????? ????????????.") {
                withData(
                    Empty(""),
                    Empty("adass"),
                ) { payload ->
                    val request = setUpHeader(payload.value)

                    val response = client.postForEntity(uri, request, TokenResponse::class.java)

                    response.statusCode shouldBe HttpStatus.BAD_REQUEST
                }
            }

            it("?????? ????????? ?????? ???, ???????????? ?????? ????????? ?????? ?????? ????????? ?????? 400 ????????? ????????????.") {
                val request = setUpHeader("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJpYXQiOiIxNTg2MzY0MzI3IiwiaXNzIjoiamluaG8uc2hpbiJ9.fWynQLZcHUgeFvFOWT8x-kdRyPmibeMRh4np81Rf9OuXVkbkFCmpdsdbDVWx_QLjdTzAnyBZHPqzKhY1gQDegA")

                val response = client.postForEntity(uri, request, TokenResponse::class.java)

                response.statusCode shouldBe HttpStatus.BAD_REQUEST
            }

            it("?????? ????????? ?????? ???, ?????? ????????? ????????? ?????? 400 ????????? ????????????.") {
                val request = setUpHeader("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzY29wZXMiOlsiUk9MRV9VU0VSIl0sImVtYWlsIjoibXNvbG8wMjEwMTVAZ21haWwuY29tIiwiaXNzIjoibW92aWVPbiIsImlhdCI6MTY0NjU2MzU4OSwiZXhwIjoxNjQ2OTIzNTg5fQ._xfSkgxVs2Qvamjq-VF5t5T7B5ALgdj-zVPxx692GY0")

                val response = client.postForEntity(uri, request, TokenResponse::class.java)

                response.statusCode shouldBe HttpStatus.BAD_REQUEST
            }

            it("?????? ????????? ?????? ???, RefreshToken??? ?????? ?????? 400 ????????? ????????????.") {
                val request = setUpHeader("Bearer $accessToken")

                val response = client.postForEntity(uri, request, TokenResponse::class.java)

                response.statusCode shouldBe HttpStatus.BAD_REQUEST
            }

            it("?????? ????????? ?????? ???, RefreshToken??? ?????? ????????? ?????? 400 ????????? ????????????.") {
                val request = setUpHeader("Bearer $expiredRefreshToken")

                val response = client.postForEntity(uri, request, TokenResponse::class.java)

                response.statusCode shouldBe HttpStatus.BAD_REQUEST
            }

            it("?????? ????????? ?????? ???, RefreshToken??? ???????????? ????????? ?????? ??? ?????? ?????? 404 ????????? ????????????.") {
                val request = setUpHeader("Bearer $userNotFoundRefreshToken")

                val response = client.postForEntity(uri, request, TokenResponse::class.java)

                response.statusCode shouldBe HttpStatus.NOT_FOUND
            }

            it("?????? ????????? ?????? ???, ????????? ????????? ???????????? ???????????? ????????? ????????????.") {
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
