package com.remember.user.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.should
import io.kotest.matchers.string.startWith
import java.lang.IllegalArgumentException

class UserSpec: StringSpec({

    "가입 확인 토큰이 일치하지 않는 경우 예외가 발생한다." {
        val sut = User.create(username = "kitty", email = "kitty@gmail.com", "1234", "token")

        val result = shouldThrow<IllegalArgumentException> { sut.registerConfirm("dd") }

        result.message should startWith("가입 확인 토큰이 일치하지 않습니다.")
    }
})
