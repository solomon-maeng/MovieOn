package com.remember.user.unit_test.domain

import com.remember.shared.contracts.commands.RegisterUserCommand
import com.remember.shared.contracts.commands.RegisteredUserConfirmCommand
import com.remember.user.domain.User

data class SetUpRegisterUserCommand(val command: RegisterUserCommand, val actual: String)
data class SetUpRegisterConfirmCommand(val command: RegisteredUserConfirmCommand, val actual: String)

fun fakeUserRepository(): FakeUserRepository {
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
