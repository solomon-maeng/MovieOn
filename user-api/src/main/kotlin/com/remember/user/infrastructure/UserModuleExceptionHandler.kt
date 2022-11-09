package com.remember.user.infrastructure

import com.remember.shared.error.BaseException
import com.remember.shared.getLogger
import com.remember.user.domain.InvariantViolation
import com.remember.user.domain.UserNotFound
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["com.remember.user"])
class UserModuleExceptionHandler {

    private val log = getLogger()

    @ExceptionHandler(
        value = [
            InvariantViolation::class,
            UserNotFound::class
        ]
    )
    fun onError(ex: BaseException): ResponseEntity<Any> {
        log.error("UserModuleError: ", ex)

        return when (ex) {
            is UserNotFound -> ResponseEntity(HttpStatus.NOT_FOUND)
            is InvariantViolation -> ResponseEntity(HttpStatus.BAD_REQUEST)
            else -> ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
