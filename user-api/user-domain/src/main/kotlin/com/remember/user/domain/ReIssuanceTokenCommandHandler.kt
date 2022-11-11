package com.remember.user.domain

import com.remember.shared.contracts.ReIssuanceTokenCommand

class ReIssuanceTokenCommandHandler(
    private val tokenReIssuer: TokenReIssuer
) {

    fun handle(command: ReIssuanceTokenCommand): Token {
        return tokenReIssuer.reIssuance(command.payload)
    }
}
