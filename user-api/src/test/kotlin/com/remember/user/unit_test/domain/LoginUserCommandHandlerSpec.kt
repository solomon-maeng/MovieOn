package com.remember.user.unit_test.domain

import com.remember.shared.contracts.LoginUserCommand
import com.remember.shared.error.BaseException
import com.remember.user.domain.LoginUserCommandHandler
import com.remember.user.domain.User
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.should
import io.kotest.matchers.string.startWith

class LoginUserCommandHandlerSpec : DescribeSpec({

    describe("LoginUserCommandHandler") {
        context("LoginUserCommand 처리 시, 유효성 검증에 실패한 경우 예외가 발생한다.") {
            withData(
                SetUpLoginUserCommand(
                    LoginUserCommand(
                        email = "kitty123@gmail.com",
                        password = "12345678!"
                    ),
                    "유저가 존재하지 않습니다."
                ),
                SetUpLoginUserCommand(
                    LoginUserCommand(
                        email = "kitty@gmail.com",
                        password = "12345678!"
                    ),
                    "가입 확인이 되지 않은 유저입니다."
                ),
            ) { (command, actual) ->
                val sut = LoginUserCommandHandler(setUpUserAndUserRepository(), FakePasswordEncrypter(), DummyTokenGenerator())

                val result = shouldThrow<BaseException> { sut.handle(command) }

                result.message should startWith(actual)
            }
        }

        it("가입된 유저이지만, 비밀번호가 다른 경우 예외가 발생한다.") {
            val command = LoginUserCommand("kitty@gmail.com", "12345678!")
            val sut = LoginUserCommandHandler(setUpConfirmUserAndUserRepository(), FakePasswordEncrypter(), DummyTokenGenerator())

            val result = shouldThrow<BaseException> { sut.handle(command) }

            result.message should startWith("비밀번호가 일치하지 않습니다.")
        }
    }
})

private fun setUpConfirmUserAndUserRepository(): FakeUserRepository {
    val userRepository = FakeUserRepository()
    val user = User.create(
        username = "rebwon",
        email = "kitty@gmail.com",
        password = "12345678!@@@",
        token = "example-token"
    )
    user.registerConfirm("example-token")
    userRepository.save(user)
    return userRepository
}

private fun setUpUserAndUserRepository(): FakeUserRepository {
    val userRepository = FakeUserRepository()
    userRepository.save(
        User.create(
            username = "rebwon",
            email = "kitty@gmail.com",
            password = "123456778!",
            token = "example-token"
        )
    )
    return userRepository
}
