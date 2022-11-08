package com.remember.app

import com.remember.support.AbstractApiSpec
import com.remember.user.domain.User
import com.remember.user.infrastructure.jpa.JpaUserRepository
import com.remember.user.interfaces.RegisterConfirmRequest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

class GET_RegisteredUserConfirmSpec(
    private val mockMvc: MockMvc,
    private val userRepository: JpaUserRepository,
) : AbstractApiSpec() {

    init {
        afterEach {
            userRepository.deleteAll()
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

private const val REGISTER_CONFIRM_URI = "/api/v1/users/register/confirm"
