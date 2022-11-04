package com.remember.user.infrastructure

import com.remember.user.domain.PasswordEncrypter
import com.remember.user.domain.UserCommandHandler
import com.remember.user.domain.UserRepository
import com.remember.user.infrastructure.jpa.UserRepositoryAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Import(PasswordEncrypterAdapter::class, UserRepositoryAdapter::class)
@Configuration
class MemberModuleConfiguration {

    @Bean
    fun userCommandHandler(
        userRepository: UserRepository,
        passwordEncrypter: PasswordEncrypter
    ): UserCommandHandler {
        return UserCommandHandler(userRepository, passwordEncrypter)
    }
}
