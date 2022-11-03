package com.remember.user.interfaces

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirements
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Tag(name = "User", description = "유저 API")
@SecurityRequirements
interface UserApiSpecification {

    @Operation(summary = "회원가입", description = "유저가 회원가입 하기 위한 EndPoint 를 제공합니다.")
    @ApiResponses(
        value = [ApiResponse(responseCode = "200", description = "회원가입 성공"),
            ApiResponse(responseCode = "400", description = "회원가입 요청 파라미터 에러")]
    )
    @PostMapping("/api/v1/users/register")
    fun register(@RequestBody @Valid request: RegisterUserRequest)

    @Operation(summary = "회원가입 확인", description = "회원가입한 유저가 올바른지 확인 하기 위한 EndPoint 를 제공합니다.")
    @ApiResponses(
        value = [ApiResponse(responseCode = "303", description = "회원가입 처리 완료 및 리다이렉트"),
            ApiResponse(responseCode = "400", description = "회원가입 확인 파라미터 에러")]
    )
    @GetMapping(value = ["/api/v1/users/register/confirm"])
    fun registerConfirm(@ModelAttribute @Valid request: RegisterConfirmRequest)

    @Operation(summary = "로그인", description = "회원가입 완료 사용자가 로그인하는 EndPoint 를 제공합니다.")
    @ApiResponses(
        value = [ApiResponse(responseCode = "200", description = "로그인 성공"),
            ApiResponse(responseCode = "400", description = "로그인 요청 파라미터 에러")]
    )
    @PostMapping("/api/v1/users/login")
    fun login(@RequestBody @Valid request: LoginUserRequest): ResponseEntity<TokenResponse>

    @Operation(summary = "토큰 재발급", description = "토큰을 재발급하는 EndPoint 를 제공합니다.")
    @ApiResponses(
        value = [ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
            ApiResponse(responseCode = "400", description = "토큰 재발급 요청 에러")]
    )
    @PostMapping("/api/v1/users/reIssuance")
    fun reIssuance(@RequestHeader("Authorization") payload: String) : ResponseEntity<TokenResponse>
}

@RestController
internal class UserApi : UserApiSpecification {

    override fun register(request: RegisterUserRequest) {

    }

    override fun registerConfirm(request: RegisterConfirmRequest) {

    }

    override fun login(request: LoginUserRequest): ResponseEntity<TokenResponse> {
        TODO("Not yet implemented")
    }

    override fun reIssuance(payload: String): ResponseEntity<TokenResponse> {
        TODO("Not yet implemented")
    }
}
