package com.remember.user.infrastructure

import com.remember.user.domain.PasswordEncrypter
import org.springframework.security.crypto.password.PasswordEncoder

class PasswordEncrypterAdapter(private val passwordEncoder: PasswordEncoder) : PasswordEncrypter {

    override fun encode(rawPassword: CharSequence): String {
        return passwordEncoder.encode(rawPassword)
    }

    override fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, encodedPassword)
    }
}
