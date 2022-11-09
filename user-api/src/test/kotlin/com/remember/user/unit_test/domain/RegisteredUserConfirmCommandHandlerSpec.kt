package com.remember.user.unit_test.domain

import com.remember.shared.contracts.commands.RegisteredUserConfirmCommand
import com.remember.shared.error.BaseException
import com.remember.user.domain.RegisteredUserConfirmCommandHandler
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.startWith
import java.util.UUID

class RegisteredUserConfirmCommandHandlerSpec: DescribeSpec({

    describe("RegisterConfirmCommandHandler") {
        context("RegisteredUserConfirmCommand 처리 시, 입력 값이 잘못된 경우 예외가 발생한다.") {
            withData(
                SetUpRegisterConfirmCommand(
                    RegisteredUserConfirmCommand(
                        token = UUID.randomUUID().toString(),
                        email = "kitty123@gmail.com"
                    ),
                    "유저가 존재하지 않습니다."
                ),
                SetUpRegisterConfirmCommand(
                    RegisteredUserConfirmCommand(
                        token = UUID.randomUUID().toString(),
                        email = "kitty@gmail.com"
                    ),
                    "가입 확인 토큰이 일치하지 않습닌다."
                ),
            ) { (command, actual) ->
                val sut = RegisteredUserConfirmCommandHandler(fakeUserRepository())

                val result = shouldThrow<BaseException> { sut.handle(command) }

                result.message should startWith(actual)
            }
        }

        it("토큰 정보가 일치하는 경우, 유효한 상태를 가지며 도메인 이벤트가 등록된다.") {
            val sut = RegisteredUserConfirmCommandHandler(fakeUserRepository())
            val command = RegisteredUserConfirmCommand(
                token = "example-token",
                email = "kitty@gmail.com"
            )

            val result = sut.handle(command)

            assertSoftly(result) {
                pollAllEvents().size shouldNotBe 0
                verified shouldBe true
            }
        }
    }
})
