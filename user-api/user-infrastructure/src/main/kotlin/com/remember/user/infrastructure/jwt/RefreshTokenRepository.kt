package com.remember.user.infrastructure.jwt

import org.springframework.data.repository.CrudRepository

interface RefreshTokenRepository : CrudRepository<RefreshToken, Long> {

    fun findByJti(jti: String): RefreshToken?
}
