package com.remember.user.domain

import com.remember.shared.contracts.commands.RegisterUserCommand
import java.util.UUID

class RegisterUserCommandHandler(
    val userRepository: UserRepository,
    val passwordEncrypter: PasswordEncrypter
) {

    fun handle(command: RegisterUserCommand): User {
        if (userRepository.existsByEmail(command.email)) throw IllegalArgumentException("이메일이 중복입니다.")
        if (userRepository.existsByUsername(command.username)) throw IllegalArgumentException("유저명이 중복입니다.")

        return userRepository.save(
            User.create(
                username = command.username,
                email = command.email,
                password = passwordEncrypter.encode(command.password),
                token = UUID.randomUUID().toString()
            )
        )
    }
}
