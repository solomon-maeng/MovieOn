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
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is null")
class User(
    @Embedded
    private var userInformation: UserInformation,

    @Embedded
    private var confirmInformation: ConfirmInformation,

    @Column(nullable = false)
    private val userKey: String = KeyGenerator.generate("USER_"),

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles")
    @Column(name = "roles")
    private val _roles: MutableSet<Role> = mutableSetOf(Role.USER),

    id: Long = 0L
) : AbstractAggregateRoot(id) {

    val userId: String
        get() = userKey
    val username: String
        get() = userInformation.username
    val email: String
        get() = userInformation.email
    val verified: Boolean
        get() = confirmInformation.verified
    val roles: Set<Role>
        get() = _roles.toSet()

    fun registerConfirm(token: String) {
        when (confirmInformation.token) {
            token -> {
                confirmInformation = ConfirmInformation(
                    token = confirmInformation.token,
                    verified = true
                )
                registerEvent(RegisterCompletedEvent(userId = userKey, email = email))
            }

            else -> throw IllegalArgumentException("가입 확인 토큰이 일치하지 않습니다.")
        }
    }

    companion object {
        fun create(username: String, email: String, password: String, token: String): User {
            val user = User(
                userInformation = UserInformation(
                    username = username,
                    email = email,
                    password = password
                ),
                confirmInformation = ConfirmInformation(token)
            )
            user.registerEvent(
                RegisteredUserEvent(
                    email = email, token = token, username = username, userId = user.userKey
                )
            )
            return user
        }
    }
}
