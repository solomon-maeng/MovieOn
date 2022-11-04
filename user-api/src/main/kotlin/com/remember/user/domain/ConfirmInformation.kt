package com.remember.user.domain

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class ConfirmInformation(
    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val token: String,

    @Column(nullable = false)
    val verified: Boolean = false
)
