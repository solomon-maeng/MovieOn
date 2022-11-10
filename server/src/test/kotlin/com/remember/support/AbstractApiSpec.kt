package com.remember.support

import com.ninjasquad.springmockk.MockkBean
import com.remember.shared.MessageBus
import io.kotest.core.spec.style.DescribeSpec
import org.springframework.boot.test.web.server.LocalServerPort

@ApiSpec
abstract class AbstractApiSpec: DescribeSpec() {

    @LocalServerPort
    protected var port: Int = 0

    @MockkBean(relaxed = true)
    protected lateinit var messageBus: MessageBus
}
