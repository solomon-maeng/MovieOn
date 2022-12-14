package com.remember.shared.unit_test

import com.remember.shared.KeyGenerator
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class KeyGeneratorSpec: StringSpec() {

    init {
        "PREFIX를 붙인 임의의 문자열 30자리를 생성한다.".config(invocations = 100) {
            KeyGenerator.generate("USER_").length shouldBe 30
        }

        "ㅇㅇ" {
            println(LocalDateTime.now().toString())
            // 2022-12-13T19:37:18.408885
            println(LocalDateTime.now().cutInFiveMin().toString())
        }
    }
}

fun LocalDateTime.cutInFiveMin(): LocalDateTime {
    val secOfOneMin = 60
    val secOfFiveMin = secOfOneMin * 5

    val checkMin = ((this.minute * secOfOneMin / secOfFiveMin) * secOfFiveMin) / secOfOneMin

    return this.withMinute(checkMin)
}

fun String.convertLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}
