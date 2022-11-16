create table refresh_tokens
(
    id         bigint       not null auto_increment,
    created_at datetime(6),
    deleted_at datetime(6),
    updated_at datetime(6),
    expired    bit          not null,
    jti        varchar(255) not null,
    primary key (id)
) engine=InnoDB;

create unique index uk_idx_jti on refresh_tokens (jti);

