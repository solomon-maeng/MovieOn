package com.remember.user.interfaces

import com.remember.shared.contracts.ReIssuanceTokenCommand
import com.remember.user.application.TokenDto
import com.remember.user.application.UserDto
import com.remember.user.application.UserFacade
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirements
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.api.annotations.ParameterObject
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
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
    fun register(@ParameterObject @RequestBody @Valid request: RegisterUserRequest): ResponseEntity<UserResponse>

    @Operation(summary = "회원가입 확인", description = "회원가입한 유저가 올바른지 확인 하기 위한 EndPoint 를 제공합니다.")
    @ApiResponses(
        value = [ApiResponse(responseCode = "303", description = "회원가입 처리 완료 및 리다이렉트"),
            ApiResponse(responseCode = "400", description = "회원가입 확인 파라미터 에러")]
    )
    @GetMapping(value = ["/api/v1/users/register/confirm"])
    fun registerConfirm(@ParameterObject @ModelAttribute @Valid request: RegisterConfirmRequest): ResponseEntity<Any>

    @Operation(summary = "로그인", description = "회원가입 완료 사용자가 로그인하는 EndPoint 를 제공합니다.")
    @ApiResponses(
        value = [ApiResponse(responseCode = "200", description = "로그인 성공"),
            ApiResponse(responseCode = "400", description = "로그인 요청 파라미터 에러")]
    )
    @PostMapping("/api/v1/users/login")
    fun login(@ParameterObject @RequestBody @Valid request: LoginUserRequest): ResponseEntity<TokenResponse>

    @Operation(summary = "토큰 재발급", description = "토큰을 재발급하는 EndPoint 를 제공합니다.")
    @ApiResponses(
        value = [ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
            ApiResponse(responseCode = "400", description = "토큰 재발급 요청 에러")]
    )
    @PostMapping("/api/v1/users/reIssuance")
    fun reIssuance(@RequestHeader("Authorization") payload: String): ResponseEntity<TokenResponse>
}

@RestController
internal class UserApi(val userFacade: UserFacade) : UserApiSpecification {

    override fun register(request: RegisterUserRequest): ResponseEntity<UserResponse> {
        val result = userFacade.execute(request.toCommand()) as UserDto
        return ResponseEntity.ok(
            UserResponse(
                userId = result.userId, email = result.email, username = result.username,
                verified = result.verified, createdAt = result.createdAt, updatedAt = result.updatedAt
            )
        )
    }

    override fun registerConfirm(request: RegisterConfirmRequest): ResponseEntity<Any> {
        userFacade.execute(request.toCommand())

        val uri = URI.create("http://localhost:3000/login")
        val header = HttpHeaders()
        header.location = uri
        return ResponseEntity(header, HttpStatus.SEE_OTHER)
    }

    override fun login(request: LoginUserRequest): ResponseEntity<TokenResponse> {
        val result = userFacade.execute(request.toCommand()) as TokenDto
        return ResponseEntity.ok(TokenResponse(result.accessToken, result.refreshToken))
    }

    override fun reIssuance(payload: String): ResponseEntity<TokenResponse> {
        val result = userFacade.execute(ReIssuanceTokenCommand(payload)) as TokenDto
        return ResponseEntity.ok(TokenResponse(result.accessToken, result.refreshToken))
    }
}
