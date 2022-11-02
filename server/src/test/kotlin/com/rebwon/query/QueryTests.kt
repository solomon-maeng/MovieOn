package com.rebwon.query

import io.kotest.core.spec.style.DescribeSpec
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
class QueryTests : DescribeSpec({

    describe("application context") {
        it("loads test") {

        }
    }
})
