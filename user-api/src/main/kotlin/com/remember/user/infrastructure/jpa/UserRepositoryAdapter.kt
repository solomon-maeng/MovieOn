package com.remember.user.infrastructure.jpa

import com.remember.user.domain.User
import com.remember.user.domain.UserRepository

class UserRepositoryAdapter(private val jpaUserRepository: JpaUserRepository) : UserRepository {

    override fun save(user: User): User {
        return jpaUserRepository.save(user)
    }

    override fun existsByEmail(email: String): Boolean {
        return jpaUserRepository.existsByConfirmInformationEmail(email)
    }

    override fun existsByUsername(username: String): Boolean {
        return jpaUserRepository.existsByUsername(username)
    }

    override fun findByEmail(email: String): User? {
        return jpaUserRepository.findByConfirmInformationEmail(email)
    }
}
