package com.remember.user.domain

class FakeUserRepository : UserRepository {

    private val data = mutableMapOf<String, User>()

    override fun save(user: User): User {
        data[user.userKey] = user
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
