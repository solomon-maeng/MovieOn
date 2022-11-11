package com.remember.user.infrastructure.jwt

object DefaultTokenExtractor {

    fun extract(header: String?): String? {
        return if (header.isNullOrBlank()) {
            null
        } else {
            if (header.length < HEADER_PREFIX.length) null
            else header.substring(HEADER_PREFIX.length)
        }
    }

    private const val HEADER_PREFIX = "Bearer "
}
