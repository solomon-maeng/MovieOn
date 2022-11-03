package com.remember.user.domain

interface UserRepository {

    fun save(user: User)
}
