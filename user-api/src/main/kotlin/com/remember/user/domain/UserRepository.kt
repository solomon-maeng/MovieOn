package com.remember.user.domain

interface UserRepository {

    fun save(user: User) : User

    fun existsByEmail(email: String) : Boolean

    fun existsByUsername(username: String) : Boolean

    fun findByEmail(email: String) : User?
}
