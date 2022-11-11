package com.remember.user.application

import com.remember.shared.MessageBus
import com.remember.shared.contracts.Command
import com.remember.shared.contracts.LoginUserCommand
import com.remember.shared.contracts.ReIssuanceTokenCommand
import com.remember.shared.contracts.RegisteredUserConfirmCommand
import com.remember.shared.contracts.RegisterUserCommand
import com.remember.user.domain.LoginUserCommandHandler
import com.remember.user.domain.ReIssuanceTokenCommandHandler
import com.remember.user.domain.RegisterUserCommandHandler
import com.remember.user.domain.RegisteredUserConfirmCommandHandler
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

@Service
class UserFacade(
    private val registerHandler: RegisterUserCommandHandler,
    private val confirmHandler: RegisteredUserConfirmCommandHandler,
    private val loginHandler: LoginUserCommandHandler,
    private val tokenHandler: ReIssuanceTokenCommandHandler,
    private val transactionTemplate: TransactionTemplate,
    private val messageBus: MessageBus
) {

    fun execute(command: Command): Any {
        return when (command) {
            is RegisterUserCommand -> {
                val user = transactionTemplate.execute { registerHandler.handle(command) }!!
                user.pollAllEvents().forEach { event -> messageBus.publish(event) }
                return UserDto(user.userId, user.username, user.email, user.verified, user.createdAt, user.updatedAt)
            }

            is RegisteredUserConfirmCommand -> {
                val user = transactionTemplate.execute { confirmHandler.handle(command) }!!
                user.pollAllEvents().forEach { event -> messageBus.publish(event) }
            }

            is LoginUserCommand -> {
                val token = transactionTemplate.execute { loginHandler.handle(command) }!!
                return TokenDto(token.accessToken, token.refreshToken)
            }

            is ReIssuanceTokenCommand -> {
                val token = transactionTemplate.execute { tokenHandler.handle(command) }!!
                return TokenDto(token.accessToken, token.refreshToken)
            }
        }
    }
}
