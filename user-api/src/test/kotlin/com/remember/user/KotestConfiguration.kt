package com.remember.user

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.spec.IsolationMode

class KotestConfiguration : AbstractProjectConfig() {

    override val isolationMode = IsolationMode.InstancePerLeaf
}
