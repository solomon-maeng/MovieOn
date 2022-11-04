package com.remember.user.domain

import com.remember.shared.KeyGenerator
import com.remember.shared.Role
import com.remember.shared.domain.model.AbstractAggregateRoot
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import java.lang.IllegalArgumentException
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "`users`")
@SQLDelete(sql = "UPDATE users SET deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is null")
class User(
    @Embedded
    private var confirmInformation: ConfirmInformation,

    @Column(nullable = false)
    val username: String,

    @Column(nullable = false)
    private val password: String,

    @Column(nullable = false, name = "user_key")
    val userKey: String = KeyGenerator.generate("USER_"),

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private val _roles: MutableSet<Role> = mutableSetOf(Role.USER),

    id: Long = 0L
) : AbstractAggregateRoot(id) {

    val email: String
        get() = confirmInformation.email
    val verified: Boolean
        get() = confirmInformation.verified
    val roles: Set<Role>
        get() = _roles.toSet()

    fun registerConfirm(token: String) {
        when (confirmInformation.token) {
            token -> {
                confirmInformation = ConfirmInformation(
                    email = email,
                    token = confirmInformation.token,
                    verified = true
                )
                registerEvent(RegisterCompletedEvent(userKey = userKey, email = email))
            }

            else -> throw IllegalArgumentException("전달된 토큰이 일치하지 않습니다.")
        }
    }

    companion object Factory {
        fun create(username: String, email: String, password: String, token: String): User {
            val confirmInformation = ConfirmInformation(email = email, token = token)
            val user = User(
                confirmInformation = confirmInformation,
                username = username,
                password = password
            )
            user.registerEvent(
                RegisteredUserEvent(
                    email = email, token = token, username = username, userKey = user.userKey
                )
            )
            return user
        }
    }
}
