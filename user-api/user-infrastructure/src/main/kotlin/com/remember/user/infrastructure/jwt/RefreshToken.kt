package com.remember.user.infrastructure.jwt

import com.remember.shared.domain.model.BaseEntity
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "REFRESH_TOKENS")
@SQLDelete(sql = "UPDATE refresh_tokens SET deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is null")
class RefreshToken(
    @Column(unique = true, nullable = false)
    val jti: String,
    private var expired: Boolean = false,
    id: Long = 0L
): BaseEntity(id) {

    fun expire() {
        if (expired) throw AlreadyExpiredToken()
        this.expired = true
    }
}
