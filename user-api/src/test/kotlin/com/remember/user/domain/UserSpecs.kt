package com.remember.user.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.kotest.matchers.string.startWith
import java.lang.IllegalArgumentException

class UserSpecs: BehaviorSpec({

    Given("User 도메인 모델") {
        val sut = User.create(username = "kitty", email = "kitty@gmail.com", "1234", "token")

        When("가입 확인 토큰이 일치하지 않는 경우,") {
            val result = shouldThrow<IllegalArgumentException> { sut.registerConfirm("dd") }

            Then("예외가 발생한다.") {
                result.message should startWith("가입 확인 토큰이 일치하지 않습니다.")
            }
        }
    }
})
