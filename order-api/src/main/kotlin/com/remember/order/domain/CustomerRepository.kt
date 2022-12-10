package com.remember.order.domain

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull
import java.lang.IllegalArgumentException

interface CustomerRepository : CrudRepository<Customer, Long>

fun CustomerRepository.getById(id: Long): Customer {
    return this.findByIdOrNull(id) ?: throw IllegalArgumentException()
}
