package com.remember.integration_test.user.api

import com.remember.support.IntegrationSpecHelper
import com.remember.user.domain.User
import com.remember.user.infrastructure.jpa.JpaUserRepository
import com.remember.user.interfaces.LoginUserRequest
import com.remember.user.interfaces.TokenResponse
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.http.HttpStatus

class POST_LoginUserSpec(
    private val userRepository: JpaUserRepository,
) : IntegrationSpecHelper() {

    init {
        beforeSpec {
            transaction.executeWithoutResult {
                val user1 = User.create("jeremy", "jeremy@gmail.com", "\$2a\$10\$f4UTUsk5eNvg59mlvvlNZ.TEaawEwO9KwzUrAEIXL4YiJSGuoklbW", "example-token")
                user1.registerConfirm("example-token")
                val user2 = User.create("rebwon", "rebwon@gmail.com", "12345678!", "example-token")
                userRepository.saveAll(mutableListOf(user1, user2))
            }
        }

        afterSpec {
            cleaner.clean()
        }

        describe("/api/v1/users/login") {
            val uri = "http://localhost:$port$LOGIN_URI"

            it("로그인 요청 시, 유저를 찾을 수 없다면 404 에러를 반환한다.") {
                val request = LoginUserRequest("kitty@gmail.com", "12345678!")

                val response = client.postForEntity(uri, request, TokenResponse::class.java)

                response.statusCode shouldBe HttpStatus.NOT_FOUND
            }

            it("로그인 요청 시, 가입 완료된 유저가 아닌 경우 400 에러를 반환한다.") {
                val request = LoginUserRequest("rebwon@gmail.com", "12345678!")

                val response = client.postForEntity(uri, request, TokenResponse::class.java)

                response.statusCode shouldBe HttpStatus.BAD_REQUEST
            }

            it("로그인 요청 시, 비밀번호가 일치하지 않는 경우 400 에러를 반환한다.") {
                val request = LoginUserRequest("jeremy@gmail.com", "12345678!@@")

                val response = client.postForEntity(uri, request, TokenResponse::class.java)

                response.statusCode shouldBe HttpStatus.BAD_REQUEST
            }

            it("로그인 요청이 정상적으로 처리되고 access, refresh 토큰이 반환된다.") {
                val request = LoginUserRequest("jeremy@gmail.com", "12345678!")

                val response = client.postForEntity(uri, request, TokenResponse::class.java)

                response.statusCode shouldBe HttpStatus.OK
                assertSoftly(response.body!!) {
                    accessToken shouldNotBe null
                    refreshToken shouldNotBe null
                }
            }
        }
    }

    companion object {
        private const val LOGIN_URI = "/api/v1/users/login"
    }
}
