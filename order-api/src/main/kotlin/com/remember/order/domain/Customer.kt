package com.remember.order.domain

import com.remember.shared.domain.model.BaseEntity
import org.hibernate.annotations.Immutable
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "users")
@Immutable
class Customer(
    var email: String,
    var username: String,
    var verified: Boolean,
): BaseEntity()
// @Immutable로 선언된 JPA 엔터티는
// 영속성 컨택스트에서 변경 감지를 처리하지 않으며
// 데이터 필드에서 setter를 호출해도 update 쿼리가 발생하지 않는다
