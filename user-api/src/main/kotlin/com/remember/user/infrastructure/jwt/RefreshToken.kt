package com.remember.user.infrastructure.jwt

import com.remember.shared.domain.model.BaseEntity
import com.remember.user.domain.InvariantViolation
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "REFRESH_TOKENS")
class RefreshToken(
    @Column(unique = true, nullable = false)
    val jti: String,
    private var expired: Boolean = false,
    id: Long = 0L
): BaseEntity(id) {

    fun expire() {
        if (expired) throw InvariantViolation("이미 만료된 토큰입니다.")
        this.expired = true
    }
}
