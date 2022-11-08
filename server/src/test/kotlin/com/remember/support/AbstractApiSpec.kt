package com.remember.support

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockHttpServletRequestDsl

@ApiSpec
abstract class AbstractApiSpec(
    private val objectMapper: ObjectMapper
) : DescribeSpec() {

    override fun isolationMode() = IsolationMode.InstancePerLeaf

    fun MockHttpServletRequestDsl.jsonBody(value: Any) {
        accept = MediaType.APPLICATION_JSON
        contentType = MediaType.APPLICATION_JSON
        content = objectMapper.writeValueAsString(value)
    }
}
