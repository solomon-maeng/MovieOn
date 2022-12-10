package com.remember.integration_test.user.query

import com.remember.order.domain.CustomerRepository
import com.remember.order.domain.CustomerService
import com.remember.order.domain.getById
import com.remember.support.IntegrationSpecHelper
import com.remember.user.domain.User
import com.remember.user.infrastructure.jpa.JpaUserRepository
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe

class ReadOnlyJpaEntitySpec(
    private val userRepository: JpaUserRepository,
    private val customerRepository: CustomerRepository,
    private val customerService: CustomerService,
): IntegrationSpecHelper() {

    init {
        lateinit var expected: User

        beforeEach {
            expected = transaction.execute {
                userRepository.save(User.create("rebwon", "rebwon@gmail.com", "pass", "example"))
            }!!
        }

        afterEach {
            cleaner.clean()
        }

        describe("유저가 존재하는 경우") {
            it("CustomerRepository로 조회가 성공한다") {
                val result = customerRepository.getById(1L)

                assertSoftly(result) {
                    expected.email shouldBe result.email
                    expected.username shouldBe result.username
                    expected.verified shouldBe result.verified
                }
            }

            it("@Immutable로 선언된 Customer 엔터티는 변경할 수 없다") {
                customerService.changeVerified(1L)

                val result = customerRepository.getById(1L)

                result.verified shouldBe false
            }
        }
    }
}
