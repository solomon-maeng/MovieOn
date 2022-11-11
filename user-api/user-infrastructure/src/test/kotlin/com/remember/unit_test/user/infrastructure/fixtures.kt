package com.remember.unit_test.user.infrastructure

import com.remember.user.infrastructure.jwt.TokenProperties

fun tokenProperties(): TokenProperties {
    return TokenProperties(
        tokenExpirationSec = 6000,
        refreshExpirationSec = 12000,
        base64TokenSigningKey = "c2VjcmV0S2V5LXRlc3QtYXV0aG9yaXphdGlvbi1qd3QtbWFuYWdlLXRva2Vu",
        tokenIssuer = "movieon"
    )
}
