package com.remember.unit_test.user.request

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.kotest.core.spec.style.DescribeSpec
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockHttpServletRequestDsl
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.filter.CharacterEncodingFilter

abstract class RequestSpecHelper : DescribeSpec() {

    protected val objectMapper = ObjectMapper()
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .registerModule(JavaTimeModule())
        .registerKotlinModule()

    protected lateinit var mockMvc: MockMvc

    protected fun mockMvcSetup(controller: Any): MockMvc {
        return MockMvcBuilders.standaloneSetup(controller)
            .addFilter<StandaloneMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .setControllerAdvice(MethodArgumentNotValidExceptionHandler())
            .setMessageConverters(MappingJackson2HttpMessageConverter(objectMapper))
            .build()
    }

    fun MockHttpServletRequestDsl.jsonBody(value: Any) {
        accept = MediaType.APPLICATION_JSON
        contentType = MediaType.APPLICATION_JSON
        content = objectMapper.writeValueAsString(value)
    }

    class MethodArgumentNotValidExceptionHandler {
        @ExceptionHandler(MethodArgumentNotValidException::class)
        fun onError(ex: MethodArgumentNotValidException): ResponseEntity<Any> {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }
}
