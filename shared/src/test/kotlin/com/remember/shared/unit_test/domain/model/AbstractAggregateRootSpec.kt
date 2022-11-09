package com.remember.shared.unit_test.domain.model

import com.remember.shared.domain.model.AbstractAggregateRoot
import com.remember.shared.domain.model.DomainEvent
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class AbstractAggregateRootSpec : DescribeSpec({

    describe("AbstractAggregateRoot") {
        context("이벤트가 등록된 상태에서,") {
            it("pollAllEvents를 호출하면 등록된 이벤트를 전부 반환하고, 기존 이벤트는 비운다.") {
                val sut = SampleAggregateRoot()
                sut.doSomething(SampleEvent())
                sut.doSomething(SampleEvent())

                val result = sut.pollAllEvents()

                result.size shouldBe 2
                sut.pollAllEvents().size shouldBe 0
            }
        }
    }
})

class SampleAggregateRoot : AbstractAggregateRoot() {

    fun doSomething(event: DomainEvent) {
        registerEvent(event)
    }
}

class SampleEvent : DomainEvent
