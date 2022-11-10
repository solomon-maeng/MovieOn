package com.remember.user.domain

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
internal data class ConfirmInformation(
    @Column(nullable = false)
    val token: String,

    @Column(nullable = false)
    val verified: Boolean = false
)
