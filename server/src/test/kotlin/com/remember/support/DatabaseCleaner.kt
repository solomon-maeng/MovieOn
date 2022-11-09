package com.remember.support

import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Profile("test")
@Component
class DatabaseCleaner(
    private val jdbcTemplate: JdbcTemplate,
) {

    fun clean() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE")
        jdbcTemplate.execute("TRUNCATE TABLE users")
        jdbcTemplate.execute("TRUNCATE TABLE user_roles")
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE")
    }

}
