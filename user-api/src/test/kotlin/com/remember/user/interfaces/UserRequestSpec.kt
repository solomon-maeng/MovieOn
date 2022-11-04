package com.remember.user.interfaces

import com.fasterxml.jackson.databind.ObjectMapper
import com.remember.user.application.UserFacade
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.datatest.withData
import io.mockk.mockk
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockHttpServletRequestDsl
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class UserRequestSpec : DescribeSpec({

    val objectMapper = ObjectMapper()
    val userFacade = mockk<UserFacade>()
    lateinit var userApiSpecification: UserApiSpecification
    lateinit var mockMvc: MockMvc

    fun MockHttpServletRequestDsl.jsonBody(value: Any) {
        accept = MediaType.APPLICATION_JSON
        contentType = MediaType.APPLICATION_JSON
        content = objectMapper.writeValueAsString(value)
    }

    beforeTest {
        userApiSpecification = UserApi(userFacade)
        mockMvc = MockMvcBuilders.standaloneSetup(userApiSpecification)
            .build()
    }

    describe("User API") {
        context("회원가입 요청 시, 입력 값이 잘못된 경우 400 BadRequest 를 반환한다.") {
            withData(
                RegisterUserRequest("", "", ""),
                RegisterUserRequest("kitty", "aaa123", "1234567!@"),
                RegisterUserRequest("kitty", "kitty123@gmail.com", "12345"),
            ) { request ->
                mockMvc.post(REGISTER_URI) {
                    jsonBody(request)
                }.andDo {
                    print()
                }.andExpect {
                    status { isBadRequest() }
                }
            }
        }

        context("회원가입 확인 요청 시, 입력 값이 잘못된 경우 400 BadRequest 를 반환한다.") {
            withData(
                RegisterConfirmRequest("", ""),
                RegisterConfirmRequest("example-token", "aaa@@@"),
            ) { (token, email) ->
                mockMvc.get(REGISTER_CONFIRM_URI) {
                    param("token", token)
                    param("email", email)
                }.andDo {
                    print()
                }.andExpect {
                    status { isBadRequest() }
                }
            }
        }

        context("로그인 요청 시, 입력 값이 잘못된 경우 400 BadRequest 를 반환한다.") {
            withData(
                LoginUserRequest("", ""),
                LoginUserRequest("aaa@@@@", "12345678!"),
                LoginUserRequest("kitty@gmail.com", "12345")
            ) { request ->
                mockMvc.post(LOGIN_URI) {
                    jsonBody(request)
                }.andDo {
                    print()
                }.andExpect {
                    status { isBadRequest() }
                }
            }
        }

        context("토근 재발급 요청 시, 인증 헤더가 비어있다면, 400 BadRequest 를 반환한다.") {
            mockMvc.post(REISSUANCE_API)
                .andDo {
                    print()
                }.andExpect {
                    status { isBadRequest() }
                }
        }
    }
})

private const val REGISTER_URI = "/api/v1/users/register"
private const val REGISTER_CONFIRM_URI = "/api/v1/users/register/confirm"
private const val LOGIN_URI = "/api/v1/users/login"
private const val REISSUANCE_API = "/api/v1/users/reIssuance"
