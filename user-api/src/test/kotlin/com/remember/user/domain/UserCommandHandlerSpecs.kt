package com.remember.user.domain

import com.remember.shared.Role
import com.remember.shared.contracts.RegisterConfirmCommand
import com.remember.shared.contracts.RegisterUserCommand
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.startWith
import java.lang.IllegalArgumentException
import java.util.*

class UserCommandHandlerSpecs : DescribeSpec({

    describe("UserCommandHandler") {
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
                val sut = UserCommandHandler(fakeUserRepository(), FakePasswordEncrypter())

                val result = shouldThrow<IllegalArgumentException> { sut.handle(command) }

                result.message should startWith(actual)
            }
        }

        it("RegisterUserCommand 처리 시, 올바른 입력이 주어지면, 유저가 반환된다.") {
            // Arrange
            val sut = UserCommandHandler(fakeUserRepository(), FakePasswordEncrypter())
            val command = RegisterUserCommand(
                username = "jiwonKim",
                email = "jiwonKim@gmail.com",
                password = "123456788!"
            )

            // Act
            val result = sut.handle(command)

            // Assert
            assertSoftly(result) {
                userId shouldNotBe null
                email shouldBe "jiwonKim@gmail.com"
                username shouldBe "jiwonKim"
                verified shouldBe false
                roles shouldContain Role.USER
                pollAllEvents().size shouldBe 1
            }
        }

        context("RegisterConfirmCommand 처리 시, 입력 값이 잘못된 경우 예외가 발생한다.") {
            withData(
                SetUpRegisterConfirmCommand(
                    RegisterConfirmCommand(
                        token = UUID.randomUUID().toString(),
                        email = "kitty123@gmail.com"
                    ),
                    "유저가 존재하지 않습니다."
                ),
                SetUpRegisterConfirmCommand(
                    RegisterConfirmCommand(
                        token = UUID.randomUUID().toString(),
                        email = "kitty@gmail.com"
                    ),
                    "가입 확인 토큰이 일치하지 않습닌다."
                ),
            ) { (command, actual) ->
                val sut = UserCommandHandler(fakeUserRepository(), FakePasswordEncrypter())

                val result = shouldThrow<IllegalArgumentException> { sut.handle(command) }

                result.message should startWith(actual)
            }
        }

        it("토큰 정보가 일치하는 경우, 유효한 상태를 가지며 도메인 이벤트가 등록된다.") {
            // Arrange
            val sut = UserCommandHandler(fakeUserRepository(), FakePasswordEncrypter())
            val command = RegisterConfirmCommand(
                token = "example-token",
                email = "kitty@gmail.com"
            )

            // Act
            val result = sut.handle(command)

            // Assert
            assertSoftly(result) {
                pollAllEvents().size shouldNotBe 0
                verified shouldBe true
            }
        }
    }
})

data class SetUpRegisterUserCommand(val command: RegisterUserCommand, val actual: String)
data class SetUpRegisterConfirmCommand(val command: RegisterConfirmCommand, val actual: String)

private fun fakeUserRepository(): FakeUserRepository {
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
