package com.remember.integration_test.user.query

import com.remember.support.DatabaseCleaner
import com.remember.support.RepositorySpec
import com.remember.user.domain.User
import com.remember.user.infrastructure.jpa.JpaUserRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe

@RepositorySpec
class UserRepositorySpec(
    private val userRepository: JpaUserRepository,
    private val cleaner: DatabaseCleaner
) : DescribeSpec() {

    init {
        lateinit var expected: User

        beforeEach {
            expected = userRepository.save(User.create("rebwon", "rebwon@gmail.com", "pass", "example"))
        }

        afterEach {
            cleaner.clean()
        }

        describe("existsByEmail 검증") {
            context("email이 존재한다면") {
                it("true를 반환한다.") {
                    userRepository.existsByUserInformationEmail("rebwon@gmail.com").shouldBeTrue()
                }
            }

            context("email이 존재하지 않는다면") {
                it("false를 반환한다.") {
                    userRepository.existsByUserInformationEmail("kitty@gmail.com").shouldBeFalse()
                }
            }
        }

        describe("existsByUsername 검증") {
            context("username이 존재한다면") {
                it("true를 반환한다.") {
                    userRepository.existsByUserInformationUsername("rebwon").shouldBeTrue()
                }
            }

            context("username이 존재하지 않는다면") {
                it("false를 반환한다.") {
                    userRepository.existsByUserInformationUsername("kitty").shouldBeFalse()
                }
            }
        }

        describe("findByEmail 검증") {
            it("이메일을 조건으로 조회하였을 때 유저 정보가 반환된다.") {
                val actual = userRepository.findByUserInformationEmail("rebwon@gmail.com")

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
}
