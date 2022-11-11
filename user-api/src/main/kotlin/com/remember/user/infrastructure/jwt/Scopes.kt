package com.remember.user.infrastructure.jwt

enum class Scopes {
    REFRESH_TOKEN;

    fun authority(): String {
        return "ROLE_" + this.name
    }
}
