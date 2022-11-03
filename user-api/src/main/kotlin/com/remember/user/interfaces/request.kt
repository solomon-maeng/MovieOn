package com.remember.user.interfaces

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

@Schema(description = "회원가입 요청")
data class RegisterUserRequest(

    @Schema(description = "유저명", example = "kitty123", required = true)
    @field:NotBlank
    val username: String,

    @Schema(description = "이메일", example = "kitty123@gmail.com", required = true)
    @field:Email @field:NotBlank
    val email: String,

    @Schema(description = "비밀번호", example = "숫자, 영어, 특수 문자 혼합 8~12자리 입력", required = true)
    @field:Pattern(regexp = "^[A-Za-z1-9~!@#$%^&*()+|=]{8,12}$")
    @field:NotBlank
    val password: String
)

@Schema(description = "회원가입 확인 요청")
data class RegisterConfirmRequest(

    @Schema(description = "인증 토큰", example = "example-token", required = true)
    @field:NotBlank
    val token: String,

    @Schema(description = "이메일", example = "kitty123@gmail.com", required = true)
    @field:NotBlank @field:Email
    val email: String
)

@Schema(description = "로그인 요청")
data class LoginUserRequest(

    @Schema(description = "이메일", example = "kitty123@gmail.com", required = true)
    @field:NotBlank
    @field:Email
    val email: String,

    @Schema(description = "비밀번호", example = "12345678!", required = true)
    @field:NotBlank
    @field:Pattern(regexp = "^[A-Za-z1-9~!@#$%^&*()+|=]{8,12}$")
    val password: String
)
