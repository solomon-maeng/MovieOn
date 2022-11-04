package com.remember.user.domain

import com.remember.shared.contracts.LoginUserCommand
import com.remember.shared.contracts.ReIssuanceTokenCommand
import com.remember.shared.contracts.RegisterConfirmCommand
import com.remember.shared.contracts.RegisterUserCommand
import java.util.*

class UserCommandHandler(
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

    fun handle(command: RegisterConfirmCommand): User {
        val user = userRepository.findByEmail(command.email)
            ?: throw IllegalArgumentException("유저가 존재하지 않습니다.")
        user.registerConfirm(command.token)
        return user
    }

    fun handle(command: LoginUserCommand) {

    }

    fun handle(command: ReIssuanceTokenCommand) {

    }
}
