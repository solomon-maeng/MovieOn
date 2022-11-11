package com.remember.unit_test.user.domain

import com.remember.user.domain.InvariantViolation
import com.remember.user.domain.User
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.should
import io.kotest.matchers.string.startWith

class UserSpec: StringSpec({

    "가입 확인 토큰이 일치하지 않는 경우 예외가 발생한다." {
        val sut = User.create(username = "kitty", email = "kitty@gmail.com", "1234", "token")

        val result = shouldThrow<InvariantViolation> { sut.registerConfirm("dd") }

        result.message should startWith("가입 확인 토큰이 일치하지 않습니다.")
    }

    "가입 확인이 되지 않은 유저인 경우 예외가 발생한다." {
        val sut = User.create(username = "kitty", email = "kitty@gmail.com", "1234", "token")

        val result = shouldThrow<InvariantViolation> { sut.beforeLoginValidate("dd", FakePasswordEncrypter()) }

        result.message should startWith("가입 확인이 되지 않은 유저입니다.")
    }
})
