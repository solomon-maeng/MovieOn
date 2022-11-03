package com.remember.playground.calculator

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.startWith

@Ignored
class StringCalculatorSpecs : DescribeSpec({

    describe("문자열 덧셈 계산기 구현") {

        context("숫자가 아닌 값이 입력되면,") {
            it("예외가 발생한다.") {
                val sut = StringCalculator("a")

                val result = shouldThrow<RuntimeException> { sut.sum() }

                result.message should startWith("숫자 이외의 값을 입력할 수 없습니다.")
            }
        }

        context("음수 값이 입력되면,") {
            it("예외가 발생한다.") {
                val sut = StringCalculator("-10")

                val result = shouldThrow<RuntimeException> { sut.sum() }

                result.message should startWith("음수 값을 입력할 수 없습니다")
            }
        }

        context("공백 값 혹은 null이 입력되면,") {
            it("0을 반환한다.") {
                val sut1 = StringCalculator("")
                val sut2 = StringCalculator(null)

                sut1.sum() shouldBe 0
                sut2.sum() shouldBe 0
            }
        }

        context("4가 입력되면,") {
            it("4를 반환한다.") {
                val sut = StringCalculator("4")

                sut.sum() shouldBe 4
            }
        }

        context("두 수가 콤마를 기준으로 입력되면,") {
            it("콤마로 분리한 두 수의 합을 반환한다.") {
                val sut = StringCalculator("2,4")

                sut.sum() shouldBe 6
            }
        }

        context("두 수가 콜론을 기준으로 입력되면,") {
            it("콜론으로 분리한 두 수의 합을 반환한다.") {
                val sut = StringCalculator("2:4")

                sut.sum() shouldBe 6
            }
        }

        context("세 수가 콤마와 콜론을 기준으로 입력되면,") {
            it("콤마와 콜론으로 분리한 세 수의 합을 반환한다.") {
                val sut = StringCalculator("1,10:3")

                sut.sum() shouldBe 14
            }
        }

        context("세 수가 복잡한 문자열과 함께 입력되면,") {
            it("문자열에 포함된 세 수의 합을 반환한다.") {
                val sut = StringCalculator("//;\n1,10:3")

                sut.sum() shouldBe 14
            }
        }

        context("네 수가 복잡한 문자열과 함께 입력되면,") {
            it("문자열에 포함된 네 수의 합을 반환한다.") {
                val sut = StringCalculator("//;\\n1;2,3:4")

                sut.sum() shouldBe 10
            }
        }
    }
})
