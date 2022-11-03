package com.remember.playground.calculator

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.startWith
import java.lang.RuntimeException

@Ignored
class NumberSpecs : DescribeSpec({

    describe("입력 값이 음수라면,") {
        it("예외가 발생한다.") {
            val result = shouldThrow<RuntimeException> { Number("-10") }
            result.message should startWith("음수 값을 입력할 수 없습니다.")
        }
    }

    describe("입력 값이 숫자가 아니라면,") {
        it("예외가 발생한다.") {
            val result = shouldThrow<RuntimeException> { Number("a") }
            result.message should startWith("숫자 이외의 값을 입력할 수 없습니다.")
        }
    }

    describe("입력 값이 공백이라면,") {
        it("0을 반환한다.") {
            Number("0") shouldBe 0
        }
    }
})
