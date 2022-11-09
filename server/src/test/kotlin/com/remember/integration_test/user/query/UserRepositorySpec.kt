package com.remember.integration_test.user.query

import com.remember.support.RepositorySpec
import com.remember.user.domain.User
import com.remember.user.infrastructure.jpa.JpaUserRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe

@RepositorySpec
class UserRepositorySpec(
    private val userRepository: JpaUserRepository
) : DescribeSpec({

    describe("UserRepository") {

        it("existsByEmail 쿼리 검증") {
            userRepository.existsByUserInformationEmail("some@gmail.com").shouldBeFalse()
        }

        it("existsByUsername 쿼리 검증") {
            userRepository.existsByUserInformationUsername("kitty").shouldBeFalse()
        }

        context("한 개의 유저 데이터가 존재하는 경우에 대한") {
            val expected = userRepository.save(User.create("kitty", "kitty@gmail.com", "pass", "token"))

            it("findByEmail 쿼리 검증") {
                val actual = userRepository.findByUserInformationEmail("kitty@gmail.com")

                expected shouldBe actual
                expected.userId shouldBe actual?.userId
                expected.username shouldBe actual?.username
                expected.email shouldBe actual?.email
                expected.createdAt shouldBe actual?.createdAt
                expected.updatedAt shouldBe actual?.updatedAt
                expected.verified shouldBe actual?.verified
            }
        }
    }
})
