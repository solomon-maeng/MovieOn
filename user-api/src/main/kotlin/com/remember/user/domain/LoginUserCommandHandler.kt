package com.remember.user.domain

import com.remember.shared.contracts.LoginUserCommand

class LoginUserCommandHandler(
    private val userRepository: UserRepository,
    private val passwordEncrypter: PasswordEncrypter,
    private val generator: TokenGenerator
) {

    fun handle(command: LoginUserCommand): Token {
        val user = userRepository.findByEmail(command.email)
            ?: throw UserNotFound("유저가 존재하지 않습니다.")
        user.validate(command.password, passwordEncrypter)
        return generator.generate(user.email, user.roles)
    }
}
