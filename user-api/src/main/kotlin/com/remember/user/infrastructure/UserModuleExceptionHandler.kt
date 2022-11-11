package com.remember.user.infrastructure

import com.remember.shared.error.BaseException
import com.remember.shared.getLogger
import com.remember.user.domain.InvariantViolation
import com.remember.user.domain.UserNotFound
import com.remember.user.infrastructure.jwt.AlreadyExpiredToken
import com.remember.user.infrastructure.jwt.InvalidArgument
import com.remember.user.infrastructure.jwt.InvalidToken
import com.remember.user.infrastructure.jwt.RefreshTokenNotFound
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["com.remember.user"])
class UserModuleExceptionHandler {

    private val log = getLogger()

    @ExceptionHandler(
        value = [
            InvariantViolation::class, UserNotFound::class,
            RefreshTokenNotFound::class, InvalidArgument::class,
            AlreadyExpiredToken::class, InvalidToken::class
        ]
    )
    fun onError(ex: BaseException): ResponseEntity<Any> {
        log.error("UserModuleError: ", ex)

        return when (ex) {
            is UserNotFound -> ResponseEntity(HttpStatus.NOT_FOUND)
            is InvariantViolation -> ResponseEntity(HttpStatus.BAD_REQUEST)
            is InvalidArgument -> ResponseEntity(HttpStatus.BAD_REQUEST)
            is AlreadyExpiredToken -> ResponseEntity(HttpStatus.BAD_REQUEST)
            is InvalidToken -> ResponseEntity(HttpStatus.BAD_REQUEST)
            is RefreshTokenNotFound -> ResponseEntity(HttpStatus.NOT_FOUND)
            else -> ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
