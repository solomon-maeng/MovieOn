package com.remember.order.domain

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
) {

    @Transactional
    fun changeVerified(id: Long) {
        val customer = customerRepository.getById(1L)

        customer.verified = true
    }
}
