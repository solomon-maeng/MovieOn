package com.remember.user.infrastructure

import com.remember.user.domain.LoginUserCommandHandler
import com.remember.user.domain.PasswordEncrypter
import com.remember.user.domain.ReIssuanceTokenCommandHandler
import com.remember.user.domain.RegisterUserCommandHandler
import com.remember.user.domain.RegisteredUserConfirmCommandHandler
import com.remember.user.domain.UserRepository
import com.remember.user.infrastructure.jpa.UserRepositoryAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Import(PasswordEncrypterAdapter::class, UserRepositoryAdapter::class)
@Configuration
class MemberModuleConfiguration {

    @Bean
    fun registerUserCommandHandler(
        userRepository: UserRepository,
        passwordEncrypter: PasswordEncrypter
    ): RegisterUserCommandHandler {
        return RegisterUserCommandHandler(userRepository, passwordEncrypter)
    }

    @Bean
    fun registerUserConfirmCommandHandler(
        userRepository: UserRepository
    ): RegisteredUserConfirmCommandHandler {
        return RegisteredUserConfirmCommandHandler(userRepository)
    }

    @Bean
    fun loginUserCommandHandler(): LoginUserCommandHandler {
        return LoginUserCommandHandler()
    }

    @Bean
    fun reIssuanceTokenCommandHandler(): ReIssuanceTokenCommandHandler {
        return ReIssuanceTokenCommandHandler()
    }
}
