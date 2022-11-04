package com.remember.user.infrastructure.jpa

import com.remember.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface JpaUserRepository : JpaRepository<User, Long> {

    fun existsByUsername(username: String): Boolean

    fun existsByConfirmInformationEmail(email: String): Boolean

    fun findByConfirmInformationEmail(email: String): User?
}
