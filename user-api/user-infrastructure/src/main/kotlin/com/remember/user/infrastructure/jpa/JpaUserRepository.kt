package com.remember.user.infrastructure.jpa

import com.remember.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface JpaUserRepository : JpaRepository<User, Long> {

    fun existsByUserInformationUsername(username: String): Boolean

    fun existsByUserInformationEmail(email: String): Boolean

    fun findByUserInformationEmail(email: String): User?
}
