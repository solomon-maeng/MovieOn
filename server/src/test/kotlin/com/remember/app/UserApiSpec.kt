package com.remember.app

import com.fasterxml.jackson.databind.ObjectMapper
import com.remember.support.AbstractApiSpec
import com.remember.user.domain.User
import com.remember.user.infrastructure.jpa.JpaUserRepository
import com.remember.user.interfaces.RegisterConfirmRequest
import com.remember.user.interfaces.RegisterUserRequest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockHttpServletRequestDsl
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

class UserApiSpec(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val userRepository: JpaUserRepository,
) : AbstractApiSpec() {
    fun MockHttpServletRequestDsl.jsonBody(value: Any) {
        accept = MediaType.APPLICATION_JSON
        contentType = MediaType.APPLICATION_JSON
        content = objectMapper.writeValueAsString(value)
    }

    init {
        afterEach {
            userRepository.deleteAll()
        }

        describe("/api/v1/users/register") {
            it("클라이언트가 회원가입에 알맞은 입력을 제공하면, 회원가입 처리 후 유저가 반환된다.") {
                val request = RegisterUserRequest("kitty", "kitty@gmail.com", "12345678!")

                mockMvc.post(REGISTER_URI) {
                    jsonBody(request)
                }.andDo {
                    print()
                }.andExpect {
                    status { isOk() }
                    jsonPath("$.userId") { exists() }
                    jsonPath("$.username") { request.username }
                    jsonPath("$.email") { request.email }
                    jsonPath("$.verified") { value(false) }
                    jsonPath("$.createdAt") { exists() }
                    jsonPath("$.updatedAt") { exists() }
                }
            }
        }

        describe("/api/v1/users/register/confirm") {
            context("회원가입 완료된 유저가 존재하며") {
                beforeEach {
                    userRepository.save(User.create("rebwon", "rebwon@gmail.com", "1234", "example-token"))
                }

                it("이메일과 토큰이 유효한 경우, 가입 완료 상태로 변경되고 로그인 페이지로 리다이렉트된다.") {
                    val request = RegisterConfirmRequest("example-token", "rebwon@gmail.com")

                    mockMvc.get(REGISTER_CONFIRM_URI) {
                        param("token", request.token)
                        param("email", request.email)
                    }.andDo {
                        print()
                    }.andExpect {
                        status { is3xxRedirection() }
                    }
                }
            }
        }
    }
}

private const val REGISTER_URI = "/api/v1/users/register"
private const val REGISTER_CONFIRM_URI = "/api/v1/users/register/confirm"
private const val LOGIN_URI = "/api/v1/users/login"
private const val REISSUANCE_API = "/api/v1/users/reIssuance"
