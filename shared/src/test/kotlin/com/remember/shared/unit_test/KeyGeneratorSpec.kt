package com.remember.shared.unit_test

import com.remember.shared.KeyGenerator
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class KeyGeneratorSpec: StringSpec() {

    init {
        "PREFIX를 붙인 임의의 문자열 30자리를 생성한다.".config(invocations = 100) {
            KeyGenerator.generate("USER_").length shouldBe 30
        }
    }
}
