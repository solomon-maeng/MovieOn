package com.remember.user.domain

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
internal data class UserInformation(
    @Column(nullable = false, unique = true)
    val username: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val password: String,
)
