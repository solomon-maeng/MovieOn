package com.remember.integration_test.user.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.remember.support.AbstractApiSpec
import com.remember.user.interfaces.RegisterUserRequest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

class POST_RegisterUserSpec(
    private val mockMvc: MockMvc,
    objectMapper: ObjectMapper,
) : AbstractApiSpec(objectMapper) {

    init {

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
    }

    companion object {

        private const val REGISTER_URI = "/api/v1/users/register"
    }
}
