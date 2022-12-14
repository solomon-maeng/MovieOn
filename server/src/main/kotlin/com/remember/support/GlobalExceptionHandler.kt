package com.remember.support

import com.remember.App
import com.remember.shared.getLogger
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.net.BindException

@RestControllerAdvice(basePackageClasses = [App::class])
class GlobalExceptionHandler : ErrorController {

    private val log = getLogger()

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun onError(ex: HttpRequestMethodNotSupportedException): ResponseEntity<Any> {
        log.debug("HttpRequestMethodNotSupportedException: {}", ex.message)
        return ResponseEntity(HttpStatus.METHOD_NOT_ALLOWED)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun onError(ex: MethodArgumentNotValidException): ResponseEntity<Any> {
        log.debug("MethodArgumentNotValidException: {}", ex.message)
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(BindException::class)
    fun onError(ex: BindException): ResponseEntity<Any> {
        log.debug("BindException: {}", ex.message)
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun onError(ex: HttpMediaTypeNotSupportedException): ResponseEntity<Any> {
        log.debug("HttpMediaTypeNotSupportedException: {}", ex.message)
        return ResponseEntity(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    }
}
