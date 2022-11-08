package com.remember.user.domain

import com.remember.shared.contracts.commands.RegisteredUserConfirmCommand

class RegisteredUserConfirmCommandHandler(val userRepository: UserRepository) {

    fun handle(command: RegisteredUserConfirmCommand): User {
        val user = userRepository.findByEmail(command.email)
            ?: throw IllegalArgumentException("유저가 존재하지 않습니다.")
        user.registerConfirm(command.token)
        return user
    }
}
