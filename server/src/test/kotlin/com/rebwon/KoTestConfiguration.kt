package com.rebwon

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.extensions.spring.SpringExtension

class KoTestConfiguration : AbstractProjectConfig() {
    override fun extensions() = listOf(SpringExtension)
}
