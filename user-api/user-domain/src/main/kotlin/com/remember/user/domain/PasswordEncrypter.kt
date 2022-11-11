package com.remember.user.domain

interface PasswordEncrypter {

    fun encode(rawPassword: CharSequence): String

    fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean
}
