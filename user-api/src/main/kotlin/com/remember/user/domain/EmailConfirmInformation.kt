package com.remember.user.domain

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class EmailConfirmInformation(
    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val emailCheckToken: String,

    val emailVerified: Boolean
)
