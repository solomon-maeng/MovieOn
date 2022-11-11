package com.remember.support

import com.ninjasquad.springmockk.MockkBean
import com.remember.shared.MessageBus
import io.kotest.core.spec.style.DescribeSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.transaction.support.TransactionTemplate

@ApiSpec
abstract class AbstractApiSpec: DescribeSpec() {

    @LocalServerPort
    protected var port: Int = 0

    @MockkBean(relaxed = true)
    protected lateinit var messageBus: MessageBus

    @Autowired protected lateinit var client: TestRestTemplate
    @Autowired protected lateinit var cleaner: DatabaseCleaner
    @Autowired protected lateinit var transaction: TransactionTemplate
}
