package com.remember.user.domain

interface TokenReIssuer {

    fun reIssuance(payload: String): Token
}
