package com.remember.shared

import org.springframework.stereotype.Component
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ThreadLocalRandom

@Component
class KeyGenerator {

    fun generate(prefix: String) : String {
        val bounded = ThreadLocalRandom.current().nextLong(ORIGIN, BOUND)
        val now = ZonedDateTime.now(ZoneId.of(ZONE_ID))
        val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN)
        return prefix + formatter.format(now) + bounded
    }

    companion object {
        private const val ZONE_ID = "Asia/Seoul"
        private const val DATE_FORMAT_PATTERN = "yyyyMMddHHmmssSSS"
        private const val ORIGIN = 10000000.toLong()
        private const val BOUND = 99999999.toLong()
    }
}
