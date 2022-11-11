package com.remember.unit_test.user.infrastructure

import com.remember.user.infrastructure.jwt.DefaultTokenExtractor
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull

class DefaultTokenExtractorSpec : StringSpec({

    "토큰 추출 시 빈 값을 넘긴 경우, null 반환한다." {
        DefaultTokenExtractor.extract("").shouldBeNull()
    }

    "토큰 추출 시 헤더 Prefix가 잘못된 경우, null 반환한다." {
        DefaultTokenExtractor.extract("ADSD").shouldBeNull()
    }
})
