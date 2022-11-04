package com.remember.shared.contracts

import com.remember.shared.domain.model.DomainEvent

interface MessageBus {

    fun publish(event: DomainEvent)
}
