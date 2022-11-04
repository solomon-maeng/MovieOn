package com.remember.support

import com.remember.shared.contracts.MessageBus
import com.remember.shared.domain.model.DomainEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.support.TransactionTemplate

@Configuration
class SupportBeanConfiguration {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun transactionTemplate(platformTransactionManager: PlatformTransactionManager): TransactionTemplate {
        val transactionTemplate = TransactionTemplate()
        transactionTemplate.transactionManager = platformTransactionManager
        transactionTemplate.isolationLevel = TransactionDefinition.ISOLATION_DEFAULT
        transactionTemplate.propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRED
        transactionTemplate.timeout = TransactionDefinition.TIMEOUT_DEFAULT
        return transactionTemplate
    }

    @Bean
    fun messageBus(eventPublisher: ApplicationEventPublisher): MessageBus {
        return DefaultMessageBus(eventPublisher)
    }

    class DefaultMessageBus(private val eventPublisher: ApplicationEventPublisher) : MessageBus {
        override fun publish(event: DomainEvent) {
            eventPublisher.publishEvent(event)
        }

    }
}
