package com.remember.support

import io.kotest.core.spec.style.DescribeSpec
import org.springframework.boot.test.web.server.LocalServerPort

@ApiSpec
abstract class AbstractApiSpec: DescribeSpec() {

    @LocalServerPort
    protected var port: Int = 0
}
