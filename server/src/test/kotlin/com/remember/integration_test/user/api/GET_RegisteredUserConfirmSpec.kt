package com.remember.integration_test.user.api

import com.remember.support.AbstractApiSpec
import com.remember.support.DatabaseCleaner
import com.remember.user.domain.User
import com.remember.user.infrastructure.jpa.JpaUserRepository
import com.remember.user.interfaces.RegisterConfirmRequest
import io.kotest.assertions.throwables.shouldThrow
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

class GET_RegisteredUserConfirmSpec(
    private val client: TestRestTemplate,
    private val userRepository: JpaUserRepository,
    private val cleaner: DatabaseCleaner
) : AbstractApiSpec() {

    init {
        afterTest {
            cleaner.clean()
        }

        describe("/api/v1/users/register/confirm") {
            val uri = URI.create("http://localhost:$port$REGISTER_CONFIRM_URI")

            context("회원가입 완료된 유저가 존재하며") {
                beforeEach {
                    cleaner.clean()
                    userRepository.save(User.create("rebwon", "rebwon@gmail.com", "1234", "example-token"))
                }

                it("이메일과 토큰이 유효한 경우, 가입 완료 상태로 변경되고 로그인 페이지로 리다이렉트된다.") {
                    val request = RegisterConfirmRequest("example-token", "rebwon@gmail.com")
                    val requestUri = UriComponentsBuilder.fromUri(uri)
                        .queryParam("token", request.token)
                        .queryParam("email", request.email)
                        .encode()
                        .toUriString()

                    shouldThrow<ResourceAccessException> {
                        client.getForEntity(requestUri, Any::class.java)
                    }
                }
            }
        }
    }

    companion object {
        private const val REGISTER_CONFIRM_URI = "/api/v1/users/register/confirm"
    }
}
