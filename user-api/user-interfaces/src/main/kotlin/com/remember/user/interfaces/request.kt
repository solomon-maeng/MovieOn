package com.remember.user.interfaces

import com.remember.shared.contracts.LoginUserCommand
import com.remember.shared.contracts.RegisteredUserConfirmCommand
import com.remember.shared.contracts.RegisterUserCommand
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

@Schema(description = "회원가입 요청")
data class RegisterUserRequest(

    @field:Schema(description = "유저명", example = "kitty123", required = true)
    @field:NotBlank
    val username: String,

    @field:Schema(description = "이메일", example = "kitty123@gmail.com", required = true)
    @field:Email @field:NotBlank
    val email: String,

    @field:Schema(description = "비밀번호", example = "숫자, 영어, 특수 문자 혼합 8~12자리 입력", required = true)
    @field:Pattern(regexp = "^[A-Za-z1-9~!@#$%^&*()+|=]{8,12}$")
    @field:NotBlank
    val password: String
) {
    fun toCommand(): RegisterUserCommand {
        return RegisterUserCommand(username = username, email = email, password = password)
    }
}

@Schema(description = "회원가입 확인 요청")
data class RegisterConfirmRequest(

    @field:Schema(description = "인증 토큰", example = "example-token", required = true)
    @field:NotBlank
    val token: String,

    @field:Schema(description = "이메일", example = "kitty123@gmail.com", required = true)
    @field:NotBlank @field:Email
    val email: String
) {
    fun toCommand(): RegisteredUserConfirmCommand {
        return RegisteredUserConfirmCommand(token = token, email = email)
    }
}

@Schema(description = "로그인 요청")
data class LoginUserRequest(

    @field:Schema(description = "이메일", example = "kitty123@gmail.com", required = true)
    @field:NotBlank
    @field:Email
    val email: String,

    @field:Schema(description = "비밀번호", example = "12345678!", required = true)
    @field:NotBlank
    @field:Pattern(regexp = "^[A-Za-z1-9~!@#$%^&*()+|=]{8,12}$")
    val password: String
) {
    fun toCommand(): LoginUserCommand {
        return LoginUserCommand(email = email, password = password)
    }
}
