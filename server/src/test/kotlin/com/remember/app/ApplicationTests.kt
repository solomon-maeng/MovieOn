package com.remember.app

import io.kotest.core.spec.style.DescribeSpec
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class ApplicationTests : DescribeSpec({


    describe("application context") {
        it("loads test") {

        }
    }
})
