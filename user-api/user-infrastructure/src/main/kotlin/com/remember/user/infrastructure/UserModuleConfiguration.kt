package com.remember.user.infrastructure

import com.remember.user.domain.LoginUserCommandHandler
import com.remember.user.domain.ReIssuanceTokenCommandHandler
import com.remember.user.domain.RegisterUserCommandHandler
import com.remember.user.domain.RegisteredUserConfirmCommandHandler
import com.remember.user.infrastructure.jpa.UserRepositoryAdapter
import com.remember.user.infrastructure.jwt.DefaultTokenGenerator
import com.remember.user.infrastructure.jwt.DefaultTokenParser
import com.remember.user.infrastructure.jwt.DefaultTokenReIssuer
import com.remember.user.infrastructure.jwt.TokenProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Import(
    value = [
        PasswordEncrypterAdapter::class, UserRepositoryAdapter::class,
        RegisterUserCommandHandler::class, RegisteredUserConfirmCommandHandler::class,
        LoginUserCommandHandler::class, DefaultTokenGenerator::class,
        ReIssuanceTokenCommandHandler::class, DefaultTokenReIssuer::class,
        DefaultTokenParser::class
    ]
)
@Configuration
@EnableConfigurationProperties(TokenProperties::class)
class UserModuleConfiguration {
}
