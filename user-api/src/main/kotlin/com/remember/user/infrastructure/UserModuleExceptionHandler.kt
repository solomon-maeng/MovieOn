package com.remember.user.infrastructure

import com.remember.shared.error.BaseException
import com.remember.shared.getLogger
import com.remember.user.domain.DuplicatedEmail
import com.remember.user.domain.DuplicatedUsername
import com.remember.user.domain.UserNotFound
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["com.remember.user"])
class UserModuleExceptionHandler {

    private val log = getLogger()

    @ExceptionHandler(value = [
        DuplicatedEmail::class,
        DuplicatedUsername::class,
        UserNotFound::class,])
    fun onError(ex: BaseException) : ResponseEntity<Any> {
        log.error("UserModuleError: ", ex)

        return when (ex) {
            is DuplicatedUsername -> ResponseEntity(HttpStatus.BAD_REQUEST)
            is DuplicatedEmail -> ResponseEntity(HttpStatus.BAD_REQUEST)
            is UserNotFound -> ResponseEntity(HttpStatus.NOT_FOUND)
            else -> ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
