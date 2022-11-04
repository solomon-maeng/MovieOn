package com.remember.shared.domain.model

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.envers.Audited
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.Transient

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AbstractAggregateRoot(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @CreationTimestamp
    @Audited
    val createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    @Audited
    val updatedAt: LocalDateTime? = null,
    @Audited
    val deletedAt: LocalDateTime? = null
) {
    @Transient
    private val _events = mutableListOf<DomainEvent>()

    protected fun registerEvent(event: DomainEvent) {
        this._events.add(event)
    }

    fun pollAllEvents() : List<DomainEvent> {
        return if (_events.isNotEmpty()) {
            val events = _events.toList()
            _events.clear()
            events
        } else {
            emptyList()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AbstractAggregateRoot

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @CreationTimestamp
    @Audited
    val createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    @Audited
    val updatedAt: LocalDateTime? = null,
    @Audited
    val deletedAt: LocalDateTime? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BaseEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
