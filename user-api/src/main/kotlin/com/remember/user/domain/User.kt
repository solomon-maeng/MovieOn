package com.remember.user.domain

import com.remember.shared.domain.model.BaseAggregateRoot
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "`users`")
@SQLDelete(sql = "UPDATE users SET deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is null")
class User(
    @Embedded
    var emailInformation: EmailConfirmInformation,

    @Column(nullable = false)
    val username: String,

    @Column(nullable = false)
    val password: String,
    id: Long = 0L
) : BaseAggregateRoot<User>(id) {


}
