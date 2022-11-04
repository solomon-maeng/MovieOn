package com.remember.support

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec

@ApiSpec
abstract class AbstractApiSpec : DescribeSpec() {

    override fun isolationMode() = IsolationMode.InstancePerLeaf
}
