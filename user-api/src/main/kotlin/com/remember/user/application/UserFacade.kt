package com.remember.user.application

import com.remember.shared.contracts.*
import com.remember.user.domain.UserCommandHandler
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

@Service
class UserFacade(
    private val userCommandHandler: UserCommandHandler,
    private val transactionTemplate: TransactionTemplate,
    private val messageBus: MessageBus
) {

    fun execute(command: Command): Any {
        return when (command) {
            is RegisterUserCommand -> {
                val user = transactionTemplate.execute { userCommandHandler.handle(command) }
                user!!.pollAllEvents().forEach { event -> messageBus.publish(event) }
                return UserDto(user.userId, user.username, user.email, user.verified, user.createdAt, user.updatedAt)
            }

            is RegisterConfirmCommand -> {
                val user = transactionTemplate.execute { userCommandHandler.handle(command) }
                user!!.pollAllEvents().forEach { event -> messageBus.publish(event) }
            }

            is LoginUserCommand -> userCommandHandler.handle(command)
            is ReIssuanceTokenCommand -> userCommandHandler.handle(command)
        }
    }
}
