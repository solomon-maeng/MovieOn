package com.remember.user.unit_test.domain

import com.remember.shared.Role
import com.remember.shared.contracts.commands.RegisterUserCommand
import com.remember.shared.error.BaseException
import com.remember.user.domain.RegisterUserCommandHandler
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.startWith

class RegisterUserCommandHandlerSpec : DescribeSpec({

    describe("RegisterUserCommandHandler") {
        context("RegisterUserCommand 처리 시, 입력 값이 잘못된 경우 예외가 발생한다.") {
            withData(
                SetUpRegisterUserCommand(
                    RegisterUserCommand(
                        username = "kitty",
                        email = "kitty@gmail.com",
                        password = "123456788!"
                    ),
                    "이메일이 중복입니다."
                ),
                SetUpRegisterUserCommand(
                    RegisterUserCommand(
                        username = "rebwon",
                        email = "kitty123@gmail.com",
                        password = "123456788!"
                    ),
                    "유저명이 중복입니다."
                ),
            ) { (command, actual) ->
                val sut = RegisterUserCommandHandler(fakeUserRepository(), FakePasswordEncrypter())

                val result = shouldThrow<BaseException> { sut.handle(command) }

                result.message should startWith(actual)
            }
        }

        it("RegisterUserCommand 처리 시, 올바른 입력이 주어지면, 유저가 반환된다.") {
            val sut = RegisterUserCommandHandler(fakeUserRepository(), FakePasswordEncrypter())
            val command = RegisterUserCommand(
                username = "jiwonKim",
                email = "jiwonKim@gmail.com",
                password = "123456788!"
            )

            val result = sut.handle(command)

            assertSoftly(result) {
                userId shouldNotBe null
                email shouldBe "jiwonKim@gmail.com"
                username shouldBe "jiwonKim"
                verified shouldBe false
                roles shouldContain Role.USER
                pollAllEvents().size shouldBe 1
            }
        }
    }
})
