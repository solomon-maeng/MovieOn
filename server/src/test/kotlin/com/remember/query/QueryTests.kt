package com.remember.query

import com.remember.support.RepositorySpec
import com.remember.user.infrastructure.jpa.JpaUserRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse

@RepositorySpec
class QueryTests(private val userRepository: JpaUserRepository) : DescribeSpec({

    describe("UserRepository") {
        it("existsByEmail 검증") {
            userRepository.existsByUserInformationEmail("some@gmail.com").shouldBeFalse()
        }

        it("existsByUsername 검증") {
            userRepository.existsByUserInformationUsername("kitty").shouldBeFalse()
        }
    }
})
