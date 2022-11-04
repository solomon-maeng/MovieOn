package com.remember.user.domain

import com.remember.shared.Role
import com.remember.shared.contracts.RegisterConfirmCommand
import com.remember.shared.contracts.RegisterUserCommand
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.startWith
import java.lang.IllegalArgumentException
import java.util.*

class UserCommandHandlerSpecs : DescribeSpec({

    describe("UserCommandHandler") {
        context("RegisterUserCommand 처리 시,") {
            it("이메일이 중복인 경우 예외가 발생한다.") {
                // Arrange
                val sut = UserCommandHandler(fakeUserRepository(), FakePasswordEncrypter())
                val command = RegisterUserCommand(
                    username = "kitty",
                    email = "kitty@gmail.com",
                    password = "123456788!"
                )

                // Act
                val result = shouldThrow<IllegalArgumentException> { sut.handle(command) }

                // Assert
                result.message should startWith("이메일이 중복입니다.")
            }

            it("유저명이 중복인 경우 예외가 발생한다.") {
                // Arrange
                val sut = UserCommandHandler(fakeUserRepository(), FakePasswordEncrypter())
                val command = RegisterUserCommand(
                    username = "rebwon",
                    email = "kitty123@gmail.com",
                    password = "123456788!"
                )

                // Act
                val result = shouldThrow<IllegalArgumentException> { sut.handle(command) }

                // Assert
                result.message should startWith("유저명이 중복입니다.")
            }

            it("올바른 입력이 주어지면, 유저가 반환된다.") {
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
                result.userKey shouldNotBe null
                result.email shouldBe "jiwonKim@gmail.com"
                result.username shouldBe "jiwonKim"
                result.verified shouldBe false
                result.roles shouldContain Role.USER
                result.pollAllEvents().size shouldBe 1
            }
        }

        context("RegisterConfirmCommand 처리 시,") {

            it("유저가 존재하지 않을 경우, 예외가 발생한다.") {
                // Arrange
                val sut = UserCommandHandler(fakeUserRepository(), FakePasswordEncrypter())
                val command = RegisterConfirmCommand(
                    token = UUID.randomUUID().toString(),
                    email = "kitty123@gmail.com"
                )

                // Act
                val result = shouldThrow<IllegalArgumentException> { sut.handle(command) }

                // Assert
                result.message should startWith("유저가 존재하지 않습니다.")
            }

            it("토큰 정보가 일치하지 않는 경우 예외가 발생한다.") {
                // Arrange
                val sut = UserCommandHandler(fakeUserRepository(), FakePasswordEncrypter())
                val command = RegisterConfirmCommand(
                    token = UUID.randomUUID().toString(),
                    email = "kitty@gmail.com"
                )

                // Act
                val result = shouldThrow<IllegalArgumentException> { sut.handle(command) }

                // Assert
                result.message should startWith("전달된 토큰이 일치하지 않습니다.")
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
                result.pollAllEvents().size shouldNotBe 0
                result.verified shouldBe true
            }
        }
    }
})

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
