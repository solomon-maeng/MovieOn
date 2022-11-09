package com.remember.integration_test.user.api

import com.remember.support.AbstractApiSpec
import com.remember.support.DatabaseCleaner
import com.remember.user.interfaces.RegisterUserRequest
import com.remember.user.interfaces.UserResponse
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.web.client.TestRestTemplate

class POST_RegisterUserSpec(
    private val client: TestRestTemplate,
    private val cleaner: DatabaseCleaner,
) : AbstractApiSpec() {

    init {
        afterTest {
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
        }
    }

    companion object {

        private const val REGISTER_URI = "/api/v1/users/register"
    }
}
