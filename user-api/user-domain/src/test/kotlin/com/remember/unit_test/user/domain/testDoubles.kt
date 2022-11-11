package com.remember.unit_test.user.domain

import com.remember.shared.Role
import com.remember.user.domain.PasswordEncrypter
import com.remember.user.domain.Token
import com.remember.user.domain.TokenGenerator
import com.remember.user.domain.User
import com.remember.user.domain.UserRepository

class FakeUserRepository : UserRepository {

    private val data = mutableMapOf<String, User>()

    override fun save(user: User): User {
        data[user.userId] = user
        return user
    }

    override fun existsByEmail(email: String): Boolean {
        return data.values.any { it.email == email }
    }

    override fun existsByUsername(username: String): Boolean {
        return data.values.any { it.username == username }
    }

    override fun findByEmail(email: String): User? {
        return data.values.firstOrNull { it.email == email }
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

class DummyTokenGenerator : TokenGenerator {
    override fun generate(email: String, roles: Set<Role>): Token {
        TODO("Not yet implemented")
    }
}
