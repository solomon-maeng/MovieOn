package com.remember.shared

import com.remember.shared.domain.model.DomainEvent

interface MessageBus {

    fun publish(event: DomainEvent)
}
