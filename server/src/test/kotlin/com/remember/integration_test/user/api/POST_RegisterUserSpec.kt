package com.remember.integration_test.user.api

import com.remember.support.AbstractApiSpec
import com.remember.support.DatabaseCleaner
import com.remember.user.domain.User
import com.remember.user.infrastructure.jpa.JpaUserRepository
import com.remember.user.interfaces.RegisterUserRequest
import com.remember.user.interfaces.UserResponse
import io.kotest.assertions.assertSoftly
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

class POST_RegisterUserSpec(
    private val client: TestRestTemplate,
    private val cleaner: DatabaseCleaner,
    private val userRepository: JpaUserRepository,
) : AbstractApiSpec() {

    init {
        beforeSpec {
            userRepository.save(User.create("rebwon", "rebwon@gmail.com", "12345678!", "example-token"))
        }

        afterSpec {
            cleaner.clean()
        }

        describe("/api/v1/users/register") {
            val uri = "http://localhost:$port$REGISTER_URI"

            it("클라이언트가 회원가입에 알맞은 입력을 제공하면, 회원가입 처리 후 유저가 반환된다.") {
                val request = RegisterUserRequest("kitty", "kitty@gmail.com", "12345678!")

                val response = client.postForObject(uri, request, UserResponse::class.java)

                assertSoftly(response) {
                    userId shouldNotBe null
                    username shouldBe request.username
                    email shouldBe request.email
                    verified shouldBe false
                    createdAt shouldNotBe null
                    updatedAt shouldNotBe null
                }
            }

            context("회원가입 요청 시, 입력 값이 중복된 경우 400 BadRequest 를 반환한다.") {
                withData(
                    RegisterUserRequest("rebwon", "happy12@gmail.com", "1234567!@"),
                    RegisterUserRequest("happy12", "rebwon@gmail.com", "1234567!@"),
                ) { request ->
                    val response = client.postForEntity(uri, request, UserResponse::class.java)

                    response.statusCode shouldBe HttpStatus.BAD_REQUEST
                }
            }
        }
    }

    companion object {

        private const val REGISTER_URI = "/api/v1/users/register"
    }
}
