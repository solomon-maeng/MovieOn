package com.remember.user.domain

import com.remember.shared.contracts.RegisteredUserConfirmCommand

class RegisteredUserConfirmCommandHandler(val userRepository: UserRepository) {

    fun handle(command: RegisteredUserConfirmCommand): User {
        val user = userRepository.findByEmail(command.email)
            ?: throw UserNotFound()
        user.registerConfirm(command.token)
        return user
    }
}
