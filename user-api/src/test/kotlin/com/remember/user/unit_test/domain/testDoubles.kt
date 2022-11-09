package com.remember.user.unit_test.domain

import com.remember.user.domain.PasswordEncrypter
import com.remember.user.domain.User
import com.remember.user.domain.UserRepository

class FakeUserRepository : UserRepository {

    private val data = mutableMapOf<String, User>()

    override fun save(user: User): User {
        data[user.userId] = user
        return user
    }

    override fun existsByEmail(email: String): Boolean {
        return data.values.any { user -> user.email == email }
    }

    override fun existsByUsername(username: String): Boolean {
        return data.values.any { user -> user.username == username }
    }

    override fun findByEmail(email: String): User? {
        return data.values.find { user -> user.email == email }
    }

}

class FakePasswordEncrypter : PasswordEncrypter {

    override fun encode(rawPassword: CharSequence): String {
        return rawPassword.toString()
    }

    override fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean {
        return rawPassword == encodedPassword
    }

}
