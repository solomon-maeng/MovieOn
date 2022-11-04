package com.remember.query

import com.remember.support.RepositorySpec
import com.remember.user.domain.User
import com.remember.user.infrastructure.jpa.JpaUserRepository
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

@RepositorySpec
class UserRepositorySpec(private val userRepository: JpaUserRepository) : DescribeSpec({

    describe("UserRepository") {
        it("existsByEmail 쿼리 검증") {
            userRepository.existsByUserInformationEmail("some@gmail.com").shouldBeFalse()
        }

        it("existsByUsername 쿼리 검증") {
            userRepository.existsByUserInformationUsername("kitty").shouldBeFalse()
        }

        it("findByEmail 쿼리 검증") {
            userRepository.findByUserInformationEmail("kitty@gmail.com") shouldBe null
        }

        it("엔터티 영속 확인") {
            val result = userRepository.save(User.create("kitty", "kitty@gmail.com", "pass", "token"))

            assertSoftly(result) {
                userId shouldNotBe null
                username shouldBe "kitty"
                email shouldBe "kitty@gmail.com"
                createdAt shouldNotBe null
                updatedAt shouldNotBe null
                verified shouldBe false
            }
        }
    }
})
