package com.remember.user.infrastructure.jwt

data class RawToken(
    val subject: String,
    val jti: String?,
    val authorities: List<String>,
) {

    fun validate(typeName: String) {
        if (isNotRefreshToken(typeName))
            throw InvalidToken("RefreshToken이 아닙니다.")
    }

    private fun isNotRefreshToken(typeName: String) = authorities.any { it != typeName }
}
